<<<<<<< HEAD
# Myhrm -- 个人练习项目

## 简介

基于springboot开发的一个简易saas企业人事管理系统

### 模块结构与技术选型

![](C:\CODER\github_rep\image\image-20220130153647755.png)



## 已完成内容

改天补上吧



## 1.25

### 1、加入feign远程调用组件, nacos注册中心

依赖搭配为springcloud(Hoxton.SR8)，springboot(2.2.6_R)，nacos(2.2.5_R);

feign使用方式：将FeignClient抽取为独立模块供所有需要的消费者使用

![](C:\CODER\github_rep\image\微信图片_20220130171758.jpg)

![image-20220130172428529](C:\CODER\github_rep\image\image-20220130172354880.png)

### 2、POI -- excel报表导入/导出工具

#### 2.1、添加employee模块下从excel批量导入用户信息功能

POI报表导入基本流程：1、读取表;

​										 2、读取行数据;

​										 3、逐行读取单元格数据 （excel表与java数据类型转换）;

​										 4、反射获取实体类对象，并为对象中excel注解下field设置值;

​										 5、将对象添加至列表，将列表中的数据写入数据库;

导入的excel需要遵守导入模板，否则抛出导入异常提示;

`excel报表导入工具类`

```java
package com.umr.myhrm_common.utils;

import com.umr.myhrm_common_model.annotation.ExcelAttribute;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExcelImportUtil<T> {
 
    private Class clazz;
    private  Field fields[];
 
    public ExcelImportUtil(Class clazz) {
        this.clazz = clazz;
        fields = clazz.getDeclaredFields();
    }
 
    /**
     * 基于注解读取excel
     */
    public List<T> readExcel(InputStream is, int rowIndex,int cellIndex) {
        List<T> list = new ArrayList<T>();
        T entity = null;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            // 不准确
            int rowLength = sheet.getLastRowNum();

            System.out.println(sheet.getLastRowNum());
            for (int rowNum = rowIndex; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                entity = (T) clazz.newInstance();
                System.out.println(row.getLastCellNum());
                for (int j = cellIndex; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    for (Field field : fields) {
                        if(field.isAnnotationPresent(ExcelAttribute.class)){
                            field.setAccessible(true);
                            ExcelAttribute ea = field.getAnnotation(ExcelAttribute.class);
                            if(j == ea.sort()) {
                                field.set(entity, covertAttrType(field, cell));
                            }
                        }
                    }
                }
                list.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
 

    /**
     * 类型转换 将cell 单元格格式转为 字段类型
     */
    private Object covertAttrType(Field field, Cell cell) throws Exception {
        String fieldType = field.getType().getSimpleName();
        if ("String".equals(fieldType)) {
            return getValue(cell);
        }else if ("Date".equals(fieldType)) {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getValue(cell)) ;
        }else if ("int".equals(fieldType) || "Integer".equals(fieldType)) {
            return Integer.parseInt(getValue(cell));
        }else if ("double".equals(fieldType) || "Double".equals(fieldType)) {
            return Double.parseDouble(getValue(cell));
        }else {
            return null;
        }
    }
 
 
    /**
     * 格式转为String
     * @param cell
     * @return
     */
    public String getValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date dt = DateUtil.getJavaDate(cell.getNumericCellValue());
                    return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(dt);
                } else {
                    // 防止数值变成科学计数法
                    String strCell = "";
                    Double num = cell.getNumericCellValue();
                    BigDecimal bd = new BigDecimal(num.toString());
                    if (bd != null) {
                        strCell = bd.toPlainString();
                    }
                    // 去除 浮点型 自动加的 .0
                    if (strCell.endsWith(".0")) {
                        strCell = strCell.substring(0, strCell.indexOf("."));
                    }
                    return strCell;
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
```

`@ExcelAttribute注解`

```
package com.umr.myhrm_common_model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用注解让表中字段与类中属性映射
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAttribute {
    /** 对应的列名称 */
    String name() default "";

    /** 列序号 */
    int sort();

    /** 字段类型对应的格式 */
    String format() default "";

}
```

`实体类中，通过@ExcelAttribute(sort = 0)注解标注excel对应字段`

```java
@ExcelAttribute(sort = 0)
private String userId;
@ExcelAttribute(sort = 1)
private String username;
private String departmentName;
@ExcelAttribute(sort = 2)
private String mobile;
@ExcelAttribute(sort = 9)
private String timeOfEntry;
private String companyId;
private String sex;
/**
 * 出生日期
 */
private String dateOfBirth;
```

`import接口`

```java
/**
 * 导入excel表数据进入user
 *
 * @param file 文件为excel表，文件名为‘file’
 * @return
 */
@PostMapping("/user/import")
public Result importUser(@RequestParam("file") MultipartFile file) throws Exception {
    //获取excel表
    Workbook wb = new XSSFWorkbook(file.getInputStream());
    Sheet sheet = wb.getSheetAt(0);
    int lastRow = sheet.getLastRowNum();
    for (int i = 1; i <= lastRow; ++i) {
        Row row = sheet.getRow(i);
        Object[] values = new Object[row.getLastCellNum()];
        for (int j = 1; j < row.getLastCellNum(); ++j) {
            Cell cell = row.getCell(j);
            values[j] = getValue(cell);
        }
        User user = new User(values);
        //调用部门服务 -- 通过code查询部门
        user.setDepartmentId(departmentClient.getByCode(user.getDepartmentId(), companyId).getId());
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.saveOne(user);
    }
    return new Result(ResultCode.SUCCESS);
}
```

#### 2.2、添加employee模块下导出某月人事报表导出为excel功能

数据查询：人事变动包括入职与离职，

逻辑与导入相反，先从数据库中读取数据，再将数据写入表，将表作为excel文件流输出到页面;

样式控制：读取模板excel，获取其中的cellstyle为数组，在生成报表时为对应cell添加样式;

超大数据量情况下，为节省内存空间，将无法支持样式控制;

`excel文件导出工具类`

```java
package com.umr.myhrm_common.utils;

import com.umr.myhrm_common_model.annotation.ExcelAttribute;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class ExcelExportUtil<T> {

    private int rowIndex;
    private int styleIndex;
    private String templatePath;
    private Class clazz;
    private  Field fields[];

    public ExcelExportUtil(Class clazz,int rowIndex,int styleIndex) {
        this.clazz = clazz;
        this.rowIndex = rowIndex;
        this.styleIndex = styleIndex;
        fields = clazz.getDeclaredFields();
    }

    /**
     * 基于注解导出
     */
    public void export(HttpServletResponse response,InputStream is, List<T> objs,String fileName) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);

        CellStyle[] styles = getTemplateStyles(sheet.getRow(styleIndex));

        AtomicInteger datasAi = new AtomicInteger(rowIndex);
        for (T t : objs) {
            Row row = sheet.createRow(datasAi.getAndIncrement());
            for(int i=0;i<styles.length;i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(styles[i]);
                for (Field field : fields) {
                    if(field.isAnnotationPresent(ExcelAttribute.class)){
                        field.setAccessible(true);
                        ExcelAttribute ea = field.getAnnotation(ExcelAttribute.class);
                        if(i == ea.sort()) {
                            if (field.get(t) != null)
                                cell.setCellValue(field.get(t).toString());
                        }
                    }
                }
            }
        }
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("ISO8859-1")));
        response.setHeader("filename", fileName);
        ServletOutputStream os = response.getOutputStream();
        workbook.write(os);
        os.flush();
    }

    public CellStyle[] getTemplateStyles(Row row) {
        CellStyle [] styles = new CellStyle[row.getLastCellNum()];
        for(int i=0;i<row.getLastCellNum();i++) {
            styles[i] = row.getCell(i).getCellStyle();
        }
        return styles;
    }
}
```

`export接口`

```java
/**
     * 导出当月人事报表
     * @param month 所选年月（yyyy-mm）
     */
    @GetMapping("/export/{month}")
    public void export(@PathVariable String month) throws Exception {
        //1、获取数据
        //模板打印数据较小规模方式
        List<EmployeeReportResult> list = userCompanyPersonService.list(month, "1");
        //创建表格 -- 采用模板打印方式
        Resource resource = new ClassPathResource("hrReportTemplate.xlsx");
        FileInputStream fis = new FileInputStream(resource.getFile());
        new ExcelExportUtil(EmployeeReportResult.class, 2, 2)
                .export(response, fis, list, month + " 人事报表.xlsx");
    }
```

*ps: 1. 在前端访问该功能api时无法弹出下载excel窗口，关闭权限校验后可以成功下载*

​          *2. 前后端日期格式有问题，暂时写死为1月*

### 3、完善permission模块逻辑 -- 自动为平台管理员与企业管理员绑定所有相关权限

*Error: 平台管理员没有companyId，在department模块和employee模块下会报错 -- 目前简单处理，抛出异常提示*

*话说admin是不是本来就不该有具体企业内部管理的权限......*



## 1.30

### 1、图片更新与保存

两种保存方式：1、直接转换成dataUrl格式存储在本地数据库；

​							优点：不需要额外的请求获取图片资源；

​							缺点：dataUrl比较大，数据库查询时压力大；

​							2、将图片存至云数据库，在本地数据库存储资源url；

​							缺点：要花钱买云数据库（不是）；

暂时使用的方式1，将请求中的图片文件按Base64解码，拼接上基本信息存储到数据库，数据库字段类型为mediumblob，数据大小上限为4m；

`上传逻辑`

```java
/**
 * 上传用户头像
 * @param id  用户id
 * @param img  图片DatUrl
 * @return  回显图片的请求url
 */
@Override
public String upload(String id, MultipartFile img) throws IOException {
    User user = userMapper.selectById(id);
    //Base64解码获得图片字节码数组
    String encode = "data:image/jpg;base64," + Base64.encode(img.getBytes());
    user.setStaffPhoto(encode);
    userMapper.updateById(user);
    return encode;
}
```



### 2、员工档案PDF导出								

主要使用JasperReport框架，主要流程：1、在jasper工具中编辑模板，动态数据部分类似于占位符；

​																	  2、数据库查找对应id的员工信息，将模板中需要的数据封装入map；

​																	  3、使用jasperprint方法生成pdf并在页面输出；

`pdf输出接口`

```java
/**
 * 输出用户信息pdf
 */
@GetMapping("/{id}/pdf")
public void getPdf(@PathVariable String id) {
    //获取pdf模板
    Resource resource = new ClassPathResource("profile.jasper");
    ServletOutputStream ops = null;
    try {
        FileInputStream fis = new FileInputStream(resource.getFile());
        ops = response.getOutputStream();
        //构造数据
        UserCompanyPerson person = userCompanyPersonService.getById(id);
        UserCompanyJobs jobs = userCompanyJobsService.getById(id);
        Map<String, Object> personMap = BeanMapUtils.beanToMap(person);
        Map<String, Object> jobMap = BeanMapUtils.beanToMap(jobs);
        Map param = new HashMap<>();
        param.putAll(personMap);
        param.putAll(jobMap);
        JasperPrint jasperPrint = JasperFillManager
                .fillReport(fis, param, new JREmptyDataSource());
        //输出pdf
        JasperExportManager.exportReportToPdfStream(jasperPrint, ops);
    } catch (IOException | JRException e) {
        e.printStackTrace();
    } finally {
        try {
            ops.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

*ps: 与excel表格导出时问题一致，关掉权限控制就能输出到页面了，postman中测试接口则没问题*

### 3、加入gateway网关组件

#### 3.1、分发请求路由

当微服务存在很多时，维护请求地址麻烦，使用网关分发路由，则前端请求只需要发送到网关服务即可；

![](C:\CODER\github_rep\image\微信图片_20220130173522.jpg)				

`路由断言配置`

```yaml
gateway:
  #路由断言工厂
  routes:
    #系统服务
    - id: myhrm-system
      uri: lb://myhrm-system
      predicates:
        - Path=/sys/**
    #企业服务
    - id: myhrm-company
      uri: lb://myhrm-company
      predicates:
        - Path=/company/**
    #雇员服务
    - id: myhrm-employee
      uri: lb://myhrm-employee
      predicates:
        - Path=/employees/**
```

*ps: 复制粘贴会报错*

#### 3.2、登录认证与权限校验

目前只完成了结合jwt进行登录认证

`gateway认证授权filter`

```java
@Order(1)
@Component
public class AuthorizationFilter implements GlobalFilter {

    @Autowired
    JwtUtils jwtUtils;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取request信息
        ServerHttpRequest request = exchange.getRequest();
        //获取请求头
        String authorization = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")) {
            //转换请求头为token
            String token = authorization.replace("Bearer ", "");
            if (!StringUtils.isEmpty(token)) {
                return chain.filter(exchange);
            }
        }
        //无此header表示尚未登录
        throw new NoResultException(ResultCode.UNAUTHENTICATED);
    }
}
```

gateway中filter无法获取到代理方法名称，原有的jwt鉴权方法行不通，直接把jwt鉴权的interceptor放至gateway模块中则会导致web与gateway冲突，比较好的解决方案是单独写一个模块进行权限认证，或者用gateway + security；

*ps: 还没解决*

#### 3.3、敏感词屏蔽等对请求与响应预处理/后处理，以及error处理功能

*ps: 暂未使用*
=======
# 1/25
改动：
1、加入了feign远程调用组件, nacos注册中心
        -- springcloud(Hoxton.SR8)，springboot(2.2.6_R)，nacos(2.2.5_R)
2、添加了employee模块下批量导入信息功能
3、添加了employee模块下生成当月报表功能
4、完善了permission模块逻辑
        -- 自动为平台管理员与企业管理员绑定所有相关权限

问题：
1、平台管理员没有companyId，在department模块和employee模块下会报错
        -- 目前简单处理，抛出异常提示
2、生成当月报表，页面无法弹出保存
        -- 数据流正常返回，问题应该处在页面代码，找不出bug，我很抱歉
>>>>>>> 34d225e8f78bee3871bcbb21981833ff990eb581
