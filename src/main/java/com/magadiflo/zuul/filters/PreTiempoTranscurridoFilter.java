package com.magadiflo.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * Como esta clase está heredando de ZuulFilter, automáticamente Spring lo
 * considera como un filtro de Zuul
 */

@Component
public class PreTiempoTranscurridoFilter extends ZuulFilter {

	private final static Logger LOG = LoggerFactory.getLogger(PreTiempoTranscurridoFilter.class);

	// Aquí valida si vamos a ejecutar o no el filtro. Si retorna true, ejecuta el
	// método run()
	@Override
	public boolean shouldFilter() {
		return true;
	}

	// Aquí se resuelve la lógica del filtro
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		LOG.info("{} request enrutado a {}", request.getMethod(), request.getRequestURL().toString());

		Long tiempoInicio = System.currentTimeMillis();
		request.setAttribute("tiempoInicio", tiempoInicio);

		return null;
	}
	
	/**
	 * pre, siempre se tiene que llamar con esa palabra clave.
	 * Recordemos que hay 3 tipos: 
	 * - pre: antes de que se resuelva la ruta (antes de que el request sea enrutado). Se usa para pasar datos al request
	 * - post: Se ejecuta después de que el request haya sido enrutado. Se usa para modificar la respuesta
	 * - route: es donde se resuelve la ruta (Se ejecuta durante el enrutado del request). Se usa para la comunicación con el microservicio
	 */

	@Override
	public String filterType() {
		return "pre"; 
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
