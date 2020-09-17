## kirms-server接口第三方权限配置

###1、配置需要拦截的接口
```yaml
kirms:
  security:
    pathPatterns:   ## 需要拦截的接口，下面为默认配置，如果需要添加，按下面格式添加
      - "/data/query"
      - "/fireapp/**"
      - "/fireWaterOpen/**"
      - "/resourceUpload/**"
    excludePathPatterns:  ## 忽略拦截的接口，比如需要拦截的接口中配置了/fireapp/**，但是其中/fireapp/test接口不需要拦截,就可以通过excludePathPatterns来忽略拦截
      - "/fireapp/test"
```

###2、第三方接入权限配置

#### 2.1、MySQL数据库配置
权限配置表**kirms_security_config**
+ auth_id: 第三方厂商身份标识,不能重复
+ secret_key: 第三方厂方密钥
+ include_path_pattern: 有权限访问的接口,多个接口以“,”分割
+ exclude_path_pattern: 无权限访问的接口,多个接口以“,”分割, 例如include_path_pattern中配置了/fireapp/**,但是其中/fireapp/test接口不想让他访问,就可以通过exclude_path_pattern来过滤
+ custom_interceptor: 自定义拦截器配置, 参考2.2自定义拦截器配置

#### 2.2、自定义拦截器配置
如果有的接口需要自定义拦截条件，需要实现**com.kedacom.kirms.interceptor.KirmsCustomInterceptor**接口,并注入到容器中,然后在配置表**custom_interceptor**配置此拦截器

```json
[
    {
        "beanName": "kirmsQueryDataInterceptor",
        "conditions": ["xhs","xfsc"],
        "path": "/data/query"
    }
]
```
+ beanName: 为KirmsCustomInterceptor实现类,注入到容器中的实例名称
+ conditions: 为拦截器中需要的条件
+ path: 此拦截器要拦截的请求接口

##### 已实现的自定义拦截器
+ com.kedacom.kirms.interceptor.KirmsQueryDataInterceptor
```text
此拦截器beanName为kirmsQueryDataInterceptor, 拦截的接口为/data/query，主要功能是控制第三方厂商可以查询哪些索引的数据，通过conditions配置来实现，例如conditions配置为["xhs","xfsc"]，则第三方厂商只能查询消火栓和消防水池的数据
```

