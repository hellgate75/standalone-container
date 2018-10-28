package com.services.container.standalone.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.services.container.standalone.factories.ComponentFactory;
import com.services.container.standalone.factories.ConfigurationFactory;

public class EngineUtilities {
	
	private static final Logger LOG = LoggerFactory.getLogger(EngineUtilities.class);
	
	public static final LogLevel LOG_LEVEL=LogLevel.VERBOSE;
	
	public static final void log(Logger logger, LogLevel level, String msg, Object... args) {
		switch (level) {
		case VERBOSE:
			if (level.compareTo(LogLevel.VERBOSE)>=0)
				logger.debug(msg, args);
			break;
		case INFO:
			if (level.compareTo(LogLevel.INFO)>=0)
				logger.info(msg, args);
			break;
		case WARN:
			if (level.compareTo(LogLevel.WARN)>=0)
				logger.warn(msg, args);
			break;
		case ERROR:
			if (level.compareTo(LogLevel.ERROR)>=0) {
				if (args.length>0)
					logger.error(msg, args);
				else
					logger.error(msg);
			}
			break;
		case NONE:
			break;
		default:
			if (level.compareTo(LogLevel.ERROR)>0)
				logger.error(msg, args);
			break;
		}
	}
	
	private static final Predicate<String> getScannerFilter() {
		return new FilterBuilder().exclude(".*.pom").exclude(".*.api_description")
				.exclude(".*.RSA").exclude(".*META-INF.*").exclude(".*.html").exclude(".*.xml").exclude(".*.properties")
				.exclude(".*.dtd").exclude(".*.gif").exclude(".*.txt");
	}

	
	protected static URL[] getAllURLs() {
		List<URL> urlList = new ArrayList<>();
		String[] urlStrings = System.getProperty("java.class.path").split(OSInfo.isWindows()?";":":");
		EngineUtilities.log(LOG, LogLevel.VERBOSE, "Full reflections url path: ", Arrays.toString(urlStrings));
		for(String f:urlStrings){
			try {
				if (f!=null && !f.trim().isEmpty()) {
					urlList.add(new File(f).toURI().toURL());
					EngineUtilities.log(LOG, LogLevel.VERBOSE, "Found URL for Relections: ", f);
				}
			} catch (IOException e) {
				EngineUtilities.log(LOG, LogLevel.ERROR, "Errorr retriving URL from classpath", e); 
			}
		}
		return urlList.toArray(new URL[0]);
	}
	
	/**
	 * Generate Full Class path Reflections Type and SubTypes scanner
	 * @return {@link ConfigurationBuilder} that realize the search
	 */
	public static final ConfigurationBuilder getFullTypeScannerReflections() {
		ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoader(EngineUtilities.class.getClassLoader())
				.addScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false))
				.filterInputsBy(getScannerFilter());
				URL[] urls = getAllURLs();
				if (urls!=null && urls.length>0)
					builder.addUrls(urls);
		builder.forPackages("");
		return builder.useParallelExecutor(Runtime.getRuntime().availableProcessors());

	}
	
	
	/**
	 * Generate Full Class path Reflections Type and SubTypes scanner
	 * @return {@link ConfigurationBuilder} that realize the search
	 */
	public static final ConfigurationBuilder getFullTypeScannerReflectionsNoClassLoader() {
		ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoader(EngineUtilities.class.getClassLoader())
				.addScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false))
				.filterInputsBy(getScannerFilter());
				URL[] urls = getAllURLs();
				if (urls!=null && urls.length>0)
					builder.addUrls(urls);
		builder.forPackages("");
		return builder.useParallelExecutor(Runtime.getRuntime().availableProcessors());

	}

	
	/**
	 * Generate Reflections Type and SubTypes scanner
	 * @param packages packages to scan or null for all
	 * @param urls source URL(s) to scan or null for all
	 * @return {@link ConfigurationBuilder} that realize the search
	 */
	public static final ConfigurationBuilder getTypeScannerBuilder(List<String> packages, List<URL> urls) {
		ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoader(ComponentFactory.class.getClassLoader())
				.addScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false))
				.filterInputsBy(getScannerFilter());
		if (packages!=null && ! packages.isEmpty() && ! packages.contains("*")){
			builder.forPackages(packages.toArray(new String[packages.size()]));
			packages.forEach( pkg -> builder.addUrls(ClasspathHelper.forPackage(pkg)) );
		}
		else if (packages!=null && ! packages.isEmpty() && packages.contains("*")) {
			builder.forPackages("");
			if (urls!=null && ! urls.isEmpty())
				builder.addUrls(urls);
			else {
				URL[] urlsD = getAllURLs();
				if (urlsD!=null && urlsD.length>0)
					builder.addUrls(urlsD);
				
			}
		}
		return builder.useParallelExecutor(Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Generate Reflections Type Method scanner
	 * @param packages packages to scan or null for all
	 * @param urls source URL(s) to scan or null for all
	 * @return {@link ConfigurationBuilder} that realize the search
	 */
	public static final ConfigurationBuilder getMethodScannerBuilder(List<String> packages, List<URL> urls) {
		ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoader(ComponentFactory.class.getClassLoader())
				.addScanners(new MethodAnnotationsScanner())
				.filterInputsBy(getScannerFilter());
		if (packages!=null && ! packages.isEmpty() && ! packages.contains("*")){
			builder.forPackages(packages.toArray(new String[packages.size()]));
			packages.forEach( pkg -> builder.addUrls(ClasspathHelper.forPackage(pkg)) );
		}
		else if (packages!=null && ! packages.isEmpty() && packages.contains("*")) {
			builder.forPackages("");
			if (urls!=null && ! urls.isEmpty())
				builder.addUrls(urls);
			else {
				URL[] urlsD = getAllURLs();
				if (urlsD!=null && urlsD.length>0)
					builder.addUrls(urlsD);
				
			}
		}
		return builder.useParallelExecutor(Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Generate Full Class path Reflections Type Method scanner
	 * @return {@link Reflections} that realize the search
	 */
	public static final ConfigurationBuilder getFullMethodScannerReflections() {
		ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoader(ConfigurationFactory.class.getClassLoader())
				.addScanners(new MethodAnnotationsScanner())
				.filterInputsBy(getScannerFilter());
				URL[] urls = getAllURLs();
				if (urls!=null && urls.length>0)
					builder.addUrls(urls);
		builder.forPackages("");
		return builder.useParallelExecutor(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Generate Reflections Type Field scanner taken from class package
	 * @param <T> Parameter type of input instance, dynamically assigned on runtime
	 * @param target Target Class for scan (not null)
	 * @return {@link ConfigurationBuilder} that realize the search
	 */
	public static final <T> ConfigurationBuilder getFieldScannerBuilderByClassPackage(T target) {
		ConfigurationBuilder builder = new ConfigurationBuilder().addClassLoader(ComponentFactory.class.getClassLoader())
				.addScanners(new FieldAnnotationsScanner())
				.filterInputsBy(getScannerFilter());
		String pkg = target.getClass().getPackage().getName();
		log(LOG, LogLevel.VERBOSE, "Executing scan on package: {}", pkg);
		builder.forPackages(pkg);
		builder.addUrls(ClasspathHelper.forPackage(pkg));
		log(LOG, LogLevel.VERBOSE, "Executing scan on url: {}", Arrays.toString(ClasspathHelper.forPackage(pkg).toArray()));
		return builder.useParallelExecutor(Runtime.getRuntime().availableProcessors());
	}

	protected EngineUtilities() {
		super();
		throw new IllegalStateException("Constants class cannot be instanziated");
	}

}
