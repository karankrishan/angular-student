package com.pdizzle.app;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * This class replaces the web.xml.  
 * @author cdpxpb
 *
 */
public class StudentServicesInit implements WebApplicationInitializer {
	private static final int EAGER_SERVLET_LOAD = 1;
	private static final String DEFAULT_CTX_MAPPING = "/*";

	@Override
	public void onStartup(final ServletContext servletCtx) throws ServletException {
	  registerFilters(servletCtx);
	  registerDispatcherServlet(servletCtx, createAppContext());
	}
	
	protected void registerFilters(final ServletContext servletCtx) {
      FilterRegistration filter = servletCtx.addFilter("CORSFilter", CorsFilter.class);
      filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true,
              DEFAULT_CTX_MAPPING);
  }

	protected AnnotationConfigWebApplicationContext createAppContext() {
		final AnnotationConfigWebApplicationContext appCtx = new AnnotationConfigWebApplicationContext();
		appCtx.register(StudentServicesConfig.class);
		return appCtx;
	}

	protected void registerDispatcherServlet(final ServletContext servletCtx,
			final AnnotationConfigWebApplicationContext appCtx) {
		ServletRegistration.Dynamic dispatcher = servletCtx.addServlet("dispatcher", new DispatcherServlet(appCtx));
		dispatcher.setLoadOnStartup(EAGER_SERVLET_LOAD);
		dispatcher.addMapping(DEFAULT_CTX_MAPPING);

	}

}
