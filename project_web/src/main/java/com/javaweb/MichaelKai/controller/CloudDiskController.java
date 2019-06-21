package com.javaweb.MichaelKai.controller;

import com.javaweb.MichaelKai.elfinder.ElFinderConstants;
import com.javaweb.MichaelKai.elfinder.command.ElfinderCommand;
import com.javaweb.MichaelKai.elfinder.command.ElfinderCommandFactory;
import com.javaweb.MichaelKai.elfinder.configuration.ElfinderConfiguration;
import com.javaweb.MichaelKai.elfinder.core.ElfinderContext;
import com.javaweb.MichaelKai.elfinder.service.ElfinderStorageFactory;
import com.javaweb.MichaelKai.shiro.ShiroKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

import com.javaweb.MichaelKai.elfinder.core.Volume;
import com.javaweb.MichaelKai.elfinder.core.VolumeSecurity;
import com.javaweb.MichaelKai.elfinder.core.impl.DefaultVolumeSecurity;
import com.javaweb.MichaelKai.elfinder.core.impl.SecurityConstraint;
import com.javaweb.MichaelKai.elfinder.param.Node;
import com.javaweb.MichaelKai.elfinder.service.ElfinderStorage;
import com.javaweb.MichaelKai.elfinder.service.VolumeSources;
import com.javaweb.MichaelKai.elfinder.service.impl.DefaultElfinderStorage;
import com.javaweb.MichaelKai.elfinder.service.impl.DefaultElfinderStorageFactory;
import com.javaweb.MichaelKai.elfinder.service.impl.DefaultThumbnailWidth;
import com.javaweb.MichaelKai.elfinder.support.locale.LocaleUtils;

/**
 * @program: project_base
 * @description: 云盘controller
 * @author: YuKai Fan
 * @create: 2019-06-19 14:15
 **/
@Controller
@RequestMapping("/api")
@Slf4j
public class CloudDiskController {

    private static final String OPEN_STREAM = "openStream";
    private static final String GET_PARAMETER = "getParameter";

    @Resource(name = "commandFactory")
    private ElfinderCommandFactory elfinderCommandFactory;

    @Autowired
    private ElfinderConfiguration elfinderConfiguration;

    /**
     * elfinder编辑器连接
     * @param request
     * @param response
     */
    @RequestMapping("/elfinder/connector/{diskFlag}")
    public void connector(@PathVariable("diskFlag") String diskFlag, HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            response.setCharacterEncoding("UTF-8");
            request = processMutipartContent(request);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        String cmd = request.getParameter(ElFinderConstants.ELFINDER_PARAMETER_COMMAND);
        ElfinderCommand elfinderCommand = elfinderCommandFactory.get(cmd);

        ElfinderStorageFactory elfinderStorageFactory = getElfinderStorageFactory(diskFlag);
        try {

            final HttpServletRequest protectRequest = request;
            elfinderCommand.execute(new ElfinderContext() {
                @Override
                public ElfinderStorageFactory getVolumeSourceFactory() {
                    return elfinderStorageFactory;
                }

                @Override
                public HttpServletRequest getRequest() {
                    return protectRequest;
                }

                @Override
                public HttpServletResponse getResponse() {
                    return response;
                }
            });

        } catch (Exception e) {
            log.error("unkown error", e);
        }
    }

    private HttpServletRequest processMutipartContent(HttpServletRequest request) throws IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return request;
        }

        Map<String, String[]> map = request.getParameterMap();

        final Map<String, Object> requestParams = new HashMap<>();
        for (String key : map.keySet()) {
            String[] obj = map.get(key);
            if (obj.length == 1) {
                requestParams.put(key, obj[0]);
            } else {
                requestParams.put(key, obj);
            }
        }

            AbstractMultipartHttpServletRequest multipartHttpServletRequest = (AbstractMultipartHttpServletRequest) request;
            ServletFileUpload servletFileUpload = new ServletFileUpload();
            String characterEncoding = request.getCharacterEncoding();
            if (characterEncoding == null) {
                characterEncoding = "UTF-8";
            }
            servletFileUpload.setHeaderEncoding(characterEncoding);

            List<MultipartFile> fileList = multipartHttpServletRequest.getFiles("upload[]");
            List<FileItemStream> list = new ArrayList<>();

            for(MultipartFile file : fileList) {
                FileItemStream item = createFileItemStream(file);
                InputStream stream = item.openStream();
                String fileName = item.getName();
                if (fileName != null && !fileName.trim().isEmpty()) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    IOUtils.copy(stream, os);
                    final byte[] bytes = os.toByteArray();
                    stream.close();

                    list.add((FileItemStream) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                            new Class[]{FileItemStream.class}, new InvocationHandler() {

                                /**
                                 * 使用反射获取FileItemStream类中openStream方法
                                 * @param proxy
                                 * @param method
                                 * @param args
                                 * @return
                                 * @throws Throwable
                                 */
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    if (OPEN_STREAM.equals(method.getName())) {
                                        return new ByteArrayInputStream(bytes);
                                    }
                                    return method.invoke(item, args);
                                }
                            }
                    ));
                }
            }

            request.setAttribute(FileItemStream.class.getName(), list);
            return (HttpServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{HttpServletRequest.class}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (GET_PARAMETER.equals(method.getName())) {
                                return requestParams.get(args[0]);
                            }
                            return method.invoke(request, args);
                        }
                    });
    }

    private FileItemStream createFileItemStream(MultipartFile file) {
        return new FileItemStream() {
            @Override
            public InputStream openStream() throws IOException {
                return file.getInputStream();
            }

            @Override
            public String getContentType() {
                return file.getContentType();
            }

            @Override
            public String getName() {
                return file.getOriginalFilename();
            }

            @Override
            public String getFieldName() {
                return file.getName();
            }

            @Override
            public boolean isFormField() {
                return false;
            }

            @Override
            public FileItemHeaders getHeaders() {
                return null;
            }

            @Override
            public void setHeaders(FileItemHeaders fileItemHeaders) {

            }
        };
    }

    private ElfinderStorageFactory getElfinderStorageFactory(String diskFlag) {
        DefaultElfinderStorageFactory elfinderStorageFactory = new DefaultElfinderStorageFactory();
        elfinderStorageFactory.setElfinderStorage(getElfinderStorage(diskFlag));
        return elfinderStorageFactory;
    }

    private ElfinderStorage getElfinderStorage(String diskFlag) {
        DefaultElfinderStorage defaultElfinderStorage = new DefaultElfinderStorage();

        // creates thumbnail
        DefaultThumbnailWidth defaultThumbnailWidth = new DefaultThumbnailWidth();
        defaultThumbnailWidth.setThumbnailWidth(elfinderConfiguration.getThumbnail().getWidth().intValue());

        // creates volumes, volumeIds, volumeLocale and volumeSecurities
        Character defaultVolumeId = 'A';
        List<Node> elfinderConfigurationVolumes = elfinderConfiguration.getVolumes();
        List<Volume> elfinderVolumes = new ArrayList<>(elfinderConfigurationVolumes.size());
        Map<Volume, String> elfinderVolumeIds = new HashMap<>(elfinderConfigurationVolumes.size());
        Map<Volume, Locale> elfinderVolumeLocales = new HashMap<>(elfinderConfigurationVolumes.size());
        List<VolumeSecurity> elfinderVolumeSecurities = new ArrayList<>();

        // creates volumes
        for (Node elfinderConfigurationVolume : elfinderConfigurationVolumes) {

            final String alias = elfinderConfigurationVolume.getAlias();
            final String source = elfinderConfigurationVolume.getSource();
            final String locale = elfinderConfigurationVolume.getLocale();
            final boolean isLocked = elfinderConfigurationVolume.getConstraint().isLocked();
            final boolean isReadable = elfinderConfigurationVolume.getConstraint().isReadable();
            final boolean isWritable = elfinderConfigurationVolume.getConstraint().isWritable();
            final String path;
            if ("disk".equals(diskFlag)) {
                path = elfinderConfigurationVolume.getPath() + ShiroKit.getUser().getId();//每个用户的云盘文件夹不同，以userId为标识
            } else {
                path = elfinderConfigurationVolume.getPath();//公共网盘
            }

            // creates new volume
            Volume volume = VolumeSources.of(source).newInstance(alias, path);

            elfinderVolumes.add(volume);
            elfinderVolumeIds.put(volume, Character.toString(defaultVolumeId));
            elfinderVolumeLocales.put(volume, LocaleUtils.toLocale(locale));

            // creates security constraint
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setLocked(isLocked);
            securityConstraint.setReadable(isReadable);
            securityConstraint.setWritable(isWritable);

            // creates volume pattern and volume security
            final String volumePattern = Character.toString(defaultVolumeId) + ElFinderConstants.ELFINDER_VOLUME_SERCURITY_REGEX;
            elfinderVolumeSecurities.add(new DefaultVolumeSecurity(volumePattern, securityConstraint));

            // prepare next volumeId character
            defaultVolumeId++;
        }

        defaultElfinderStorage.setThumbnailWidth(defaultThumbnailWidth);
        defaultElfinderStorage.setVolumes(elfinderVolumes);
        defaultElfinderStorage.setVolumeIds(elfinderVolumeIds);
        defaultElfinderStorage.setVolumeLocales(elfinderVolumeLocales);
        defaultElfinderStorage.setVolumeSecurities(elfinderVolumeSecurities);

        return defaultElfinderStorage;
    }
}