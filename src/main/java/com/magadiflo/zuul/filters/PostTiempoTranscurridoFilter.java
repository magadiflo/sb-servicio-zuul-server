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
public class PostTiempoTranscurridoFilter extends ZuulFilter {

	private final static Logger LOG = LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);

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

		LOG.info("Entrando a post filter");

		Long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
		Long tiempoFinal = System.currentTimeMillis();
		Long tiempoTranscurrido = tiempoFinal - tiempoInicio;
		LOG.info("Tiempo transcurrido {} en segundos", tiempoTranscurrido.doubleValue()/1000.00);
		LOG.info("Tiempo transcurrido {} en milisegundos", tiempoTranscurrido);

		return null;
	}
	
	/**
	 * Recordemos que hay 3 tipos: 
	 * - pre: antes de que se resuelva la ruta (antes de que el request sea enrutado). Se usa para pasar datos al request
	 * - post: Se ejecuta después de que el request haya sido enrutado. Se usa para modificar la respuesta
	 * - route: es donde se resuelve la ruta (Se ejecuta durante el enrutado del request). Se usa para la comunicación con el microservicio
	 * - Error: otro tipo de filtro que permite procesar y manejar errores
	 */

	@Override
	public String filterType() {
		return "post"; 
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
