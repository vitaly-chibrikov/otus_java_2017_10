package ru.otus.l62.b1.chain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class IntercepterChaining {

    interface Intercepter {
        void doNext(HttpServletRequest request, HttpServletResponse response) throws Exception;
    }

    static abstract class AbstractIntercepter implements Intercepter {
        private Intercepter next;

        void setNext(Intercepter intercepter) {
            next = intercepter;
        }

        @Override
        public void doNext(HttpServletRequest request, HttpServletResponse response) throws Exception {
            // Исполняем свою часть
            execute(request, response);

            // Отдаем дальше
            if (next != null) {
                next.doNext(request, response);
            }
        }

        abstract void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;

    }

    private static AbstractIntercepter buildChain(AbstractIntercepter... interceptors) {
        AbstractIntercepter current = null;
        for (AbstractIntercepter interceptor : interceptors) {
            if (current != null) {
                current.setNext(interceptor);
            }
            current = interceptor;
        }

        return interceptors[0];
    }

    public static void main(String[] args) throws Exception {
        buildChain(
                new LogIntercepter(),
//                new LoginIntercepter(),
                new DetectRequestIntercepter()
        ).doNext(null, null);
    }

    static class LogIntercepter extends AbstractIntercepter {
        @Override
        void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("> logging");
        }
    }

    static class LoginIntercepter extends AbstractIntercepter {
        @Override
        void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("> check login");
        }
    }

    static class DetectRequestIntercepter extends AbstractIntercepter {
        @Override
        void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("> detect command");
        }
    }
}
