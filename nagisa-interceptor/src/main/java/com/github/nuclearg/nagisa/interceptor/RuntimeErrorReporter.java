package com.github.nuclearg.nagisa.interceptor;

/**
 * 运行期错误报告器
 * 
 * @author ng
 *
 */
class RuntimeErrorReporter {
    void report(String msg) {
        System.err.println(msg);
    }
}
