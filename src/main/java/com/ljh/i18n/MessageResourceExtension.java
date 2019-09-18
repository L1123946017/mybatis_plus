package com.ljh.i18n;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("messageSource")
public class MessageResourceExtension extends ResourceBundleMessageSource {
    private final static Logger logger = LoggerFactory.getLogger(MessageResourceExtension.class);

    private static final String PROPERTIESSUFFIX = ".properties";
    private static final String CLASSES = "/classes/";
    private static final String CLASSES_BANG = "/classes!/";
    private static final String CLASSPATH = "classpath:";

    /**
     *  * 指定的国际化文件
     *  
     */
    @Value(value = "${spring.messages.baseProperties:i18n}")
    private String baseProperties;
    /**
     *  * 父MessageSource指定的国际化文件
     *  
     */
    @Value(value = "${spring.messages.basename:messages}")
    private String basename;

    @Value(value = "${spring.messages.encoding:UTF-8}")
    private String encoding;

    @Value(value = "${spring.messages.cache-duration:-1}")
    private int cacheSeconds;
    @Value(value = "${spring.messages.use-code-as-default-message:true}")
    private Boolean useCodeAsDefaultMessage;

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();


    @PostConstruct
    public void init() {
        if (!StringUtils.isEmpty(baseProperties)) {
            String[] baseNames = getAllBaseNames(baseProperties);
            this.setBasenames(baseNames);
        }
        // 设置父MessageSource
        ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
        parent.setBasename(basename);
        this.setDefaultEncoding(encoding);
        this.setCacheSeconds(cacheSeconds);
        this.setUseCodeAsDefaultMessage(useCodeAsDefaultMessage);
        this.setParentMessageSource(parent);
    }


    /**
     * 获取文件夹下所有的国际化文件名
     *
     * @param propertiesName 文件名
     * @return
     */
    private String[] getAllBaseNames(String propertiesName) {
        List<String> baseNames = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources(propertiesName);
            for (int j = 0, resLen = resources.length; j < resLen; j++) {
                Resource resource = resources[j];
                String baseName = getBaseName(resource);
                if (baseName != null && !baseName.contains("_")) {
                    baseNames.add(baseName);
                }
            }
        } catch (IOException e) {
            logger.error("No message source files found for basename " + basename + ".");
        }
        return baseNames.toArray(new String[baseNames.size()]);
    }


    private String getBaseName(Resource resource) throws IOException {
        String baseName = null;
        String uri = resource.getURI().toString();
        if(!uri.endsWith(PROPERTIESSUFFIX)){
            return baseName;
        }
        /*
         * On jBOSS application server the property files are stored in virtual
         * file system so the resource type wil be VfsResource. On local it will
         * be FileSystemResource. We are using OR condition so that it will work
         * on both local and jBOSS without any issues
         */
        if (resource instanceof FileSystemResource || resource instanceof VfsResource) {
            baseName = StringUtils.substringBetween(uri, CLASSES, PROPERTIESSUFFIX);
        } else if (resource instanceof ClassPathResource) {
            baseName = StringUtils.substringBetween(uri, CLASSES_BANG, PROPERTIESSUFFIX);
        } else if (resource instanceof UrlResource) {
            baseName = CLASSPATH + StringUtils.substringBetween(uri, ".jar!/", PROPERTIESSUFFIX);
        } else {
            logger.info("URI not matched any of the resource types..so just reading it as a string");
            baseName = CLASSPATH + StringUtils.substringBetween(uri, CLASSES, PROPERTIESSUFFIX);
        }
        return baseName;
    }


    /**
     * 遍历所有文件
     *
     * @param basenames
     * @param folder
     * @param path 
     */
    private void getAllFile(List<String> basenames, File folder, String path) {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                this.getAllFile(basenames, file, path + folder.getName() + File.separator);
            }
        } else {
            String i18Name = this.getI18FileName(path + folder.getName());
            if (!basenames.contains(i18Name)) {
                basenames.add(i18Name);
            }

        }
    }


    /**
     * 把普通文件名转换成国际化文件名
     *
     * @param filename
     * @return 
     */
    private String getI18FileName(String filename) {
        filename = filename.replace(".properties", "");
        for (int i = 0; i < 2; i++) {
            int index = filename.lastIndexOf("_");
            if (index != -1) {
                filename = filename.substring(0, index);
            }
        }
        return filename;
    }

}