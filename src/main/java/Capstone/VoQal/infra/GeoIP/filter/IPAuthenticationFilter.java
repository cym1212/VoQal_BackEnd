package Capstone.VoQal.infra.GeoIP.filter;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

@Component
@Slf4j
@RequiredArgsConstructor
public class IPAuthenticationFilter implements Filter {

    private final DatabaseReader databaseReader;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String country = null;
        try {
            country = databaseReader.country(inetAddress).getCountry().getName();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
        if (country == null || !country.equals("South Korea")) {
            log.info("Access Rejected : {}, {}", ipAddress, country);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("IP Authentication Filter Init..");
    }

    @Override
    public void destroy() {
        log.info("IP Authentication Filter Destroy..");
    }
}
