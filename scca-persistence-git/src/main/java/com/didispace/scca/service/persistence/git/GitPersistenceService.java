package com.didispace.scca.service.persistence.git;

import com.didispace.easyutils.cmd.CmdRunner;
import com.didispace.easyutils.file.FileUtils;
import com.didispace.easyutils.file.PropertiesUtils;
import com.didispace.scca.core.service.PersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by 程序猿DD/翟永超 on 2018/4/24.
 * <p>
 * Blog: http://blog.didispace.com/
 * Github: https://github.com/dyc87112/
 */
@Slf4j
public class GitPersistenceService implements PersistenceService {

    @Autowired
    private GitProperties gitProperties;

    @Override
    public void updateProperties(String name, String profile, String label, Properties update) {
        int index = name.indexOf(".git");
        String projectUrl = name.substring(0, index + 4);
        String propertiesFile = name.substring(index + 5);

        // 生成本地拉取配置用来修改使用的唯一目录名
        String dir = UUID.randomUUID().toString();
        // 获取要修改文件的相对路径
        String path = dir + "/" + propertiesFile;
        log.debug("update file : " + path);

        // projectUrl append username & password， git clone projectUrl
        projectUrl = projectUrl.replace("http://",
                "http://" + gitProperties.getUsername() + ":" + gitProperties.getPassword() + "@");
        log.debug("projectUrl : " + projectUrl);

        try {
            // git clone properites from git
            CmdRunner.execute("git clone " + projectUrl + " " + dir);

            // git checkout branch(label)
            CmdRunner.execute("git checkout " + label, new File(dir));

            // read propertiesFile before upddate, write properties, read propertiesFile after update
            log.debug("---------------- properties before update ----------------");
            PropertiesUtils.printProperties(path, true);
            log.debug("---------------- properties after update ----------------");
            PropertiesUtils.printProperties(update, true);

            // store update properties
            PropertiesUtils.store(update, path, "Write by scca, more information see : https://github.com/dyc87112/spring-cloud-config-admin");

            // commit & push
            CmdRunner.execute("git add .", new File(dir));
            CmdRunner.execute("git commit -m 'update'", new File(dir));
            CmdRunner.execute("git push", new File(dir));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.deleteDirectory(new File(dir));
        }


    }

    @Override
    public Properties readPropertiesTemplate(String applicationName) {
        // TODO
        return null;
    }


}
