package codex.terry.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-28
 * 文件描述:
 */
public class PathFilter implements Filter{

    private static final Logger LOG = LoggerFactory.getLogger(PathFilter.class);
    private FilterConfig config;

    @Override
    public void init(final FilterConfig config) throws ServletException {
        this.config = config;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse resp, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String basePath = request.getScheme()
                + "://" + request.getServerName()
                + ":" + request.getServerPort()
                + request.getContextPath();
        String servletPath = request.getServletPath();
        LOG.info("[PATH]: " + basePath + servletPath);

        chain.doFilter(request, response);
    }

}
