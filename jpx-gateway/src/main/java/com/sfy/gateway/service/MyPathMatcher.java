package com.sfy.gateway.service;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by 金鹏祥 on 2019/6/13.
 */
@Service
public class MyPathMatcher extends AntPathMatcher {

    public boolean matches(String lookupPath, List<String> includePatterns) {

        if (ObjectUtils.isEmpty(includePatterns)) {
            return false;
        } else {
            for (String pattern : includePatterns) {
                if (match(pattern, lookupPath)) {
                    return true;
                }
            }
            return false;
        }
    }
}
