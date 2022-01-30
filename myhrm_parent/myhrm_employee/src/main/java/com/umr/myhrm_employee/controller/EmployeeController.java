package com.umr.myhrm_employee.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.umr.myhrm_common.controller.BaseController;
import com.umr.myhrm_common.entity.PageResult;
import com.umr.myhrm_common.entity.Result;
import com.umr.myhrm_common.entity.ResultCode;
import com.umr.myhrm_common.utils.BeanMapUtils;
import com.umr.myhrm_common.utils.ExcelExportUtil;
import com.umr.myhrm_common_model.domain.employee.*;
import com.umr.myhrm_common_model.domain.employee.response.EmployeeReportResult;
import com.umr.myhrm_employee.service.*;
import net.sf.jasperreports.engine.*;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    @Autowired
    private UserCompanyPersonService userCompanyPersonService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;

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

    /**
     * 员工个人信息保存
     */
    @PutMapping("/{id}/personalInfo")
    public Result savePersonalInfo(@PathVariable("id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPerson sourceInfo = new UserCompanyPerson();
        BeanUtils.populate(sourceInfo, map);
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @GetMapping("/{id}/personalInfo")
    public Result findPersonalInfo(@PathVariable("id") String uid) throws Exception {
        UserCompanyPerson info = userCompanyPersonService.getById(uid);
        if(info == null) {
            info = new UserCompanyPerson();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 员工岗位信息保存
     */
    @PutMapping("/{id}/jobs")
    public Result saveJobsInfo(@PathVariable("id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @GetMapping("/{id}/jobs")
    public Result findJobsInfo(@PathVariable("id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.getById(super.userId);
        if(info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 离职表单保存
     */
    @PutMapping("/{id}/leave")
    public Result saveLeave(@PathVariable("id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @GetMapping("/{id}/leave")
    public Result findLeave(@PathVariable("id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.getById(uid);
        if(resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,resignation);
    }

    /**
     * 导入员工
     */
    @PostMapping("/import")
    public Result importDatas(@RequestParam("file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @PutMapping("/{id}/transferPosition")
    public Result saveTransferPosition(@PathVariable("id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @GetMapping("/{id}/transferPosition")
    public Result findTransferPosition(@PathVariable("id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.getById(uid);
        if(jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @PutMapping("/{id}/positive")
    public Result savePositive(@PathVariable("id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @GetMapping("/{id}/positive")
    public Result findPositive(@PathVariable("id") String uid) throws Exception {
        EmployeePositive positive = positiveService.getById(uid);
        if(positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,positive);
    }

    /**
     * 导出当月人事报表
     * @param month 所选年月（yyyy-mm）
     */
    @GetMapping("/export/{month}")
    public void export(@PathVariable String month) throws Exception {
        //百万数据处理方式
        //        //1.获取报表数据
//        List<EmployeeReportResult> list = userCompanyPersonService.list(month, "1");
//        //2.构造Excel
//        //创建工作簿
//        //SXSSFWorkbook : 百万数据报表 -- 不支持模板打印
//        //Workbook wb = new XSSFWorkbook();
//        SXSSFWorkbook wb = new SXSSFWorkbook(100); //阈值，内存中的对象数量最大数量
//        //构造sheet
//        Sheet sheet = wb.createSheet();
//        //创建行
//        //标题
//        String [] titles = "编号,姓名,手机,最高学历,国家地区,护照号,籍贯,生日,属相,入职时间,离职类型,离职原因,离职时间".split(",");
//        //处理标题
//        Row row = sheet.createRow(0);
//        int titleIndex=0;
//        for (String title : titles) {
//            Cell cell = row.createCell(titleIndex++);
//            cell.setCellValue(title);
//        }
//        int rowIndex = 1;
//        Cell cell=null;
//        for(int i=0;i<10000;i++){
//            for (EmployeeReportResult employeeReportResult : list) {
//                row = sheet.createRow(rowIndex++);
//                // 编号,
//                cell = row.createCell(0);
//                cell.setCellValue(employeeReportResult.getUserId());
//                // 姓名,
//                cell = row.createCell(1);
//                cell.setCellValue(employeeReportResult.getUsername());
//                // 手机,
//                cell = row.createCell(2);
//                cell.setCellValue(employeeReportResult.getMobile());
//                // 最高学历,
//                cell = row.createCell(3);
//                cell.setCellValue(employeeReportResult.getTheHighestDegreeOfEducation());
//                // 国家地区,
//                cell = row.createCell(4);
//                cell.setCellValue(employeeReportResult.getNationalArea());
//                // 护照号,
//                cell = row.createCell(5);
//                cell.setCellValue(employeeReportResult.getPassportNo());
//                // 籍贯,
//                cell = row.createCell(6);
//                cell.setCellValue(employeeReportResult.getNativePlace());
//                // 生日,
//                cell = row.createCell(7);
//                cell.setCellValue(employeeReportResult.getBirthday());
//                // 属相,
//                cell = row.createCell(8);
//                cell.setCellValue(employeeReportResult.getZodiac());
//                // 入职时间,
//                cell = row.createCell(9);
//                cell.setCellValue(employeeReportResult.getTimeOfEntry());
//                // 离职类型,
//                cell = row.createCell(10);
//                cell.setCellValue(employeeReportResult.getTypeOfTurnover());
//                // 离职原因,
//                cell = row.createCell(11);
//                cell.setCellValue(employeeReportResult.getReasonsForLeaving());
//                // 离职时间
//                cell = row.createCell(12);
//                cell.setCellValue(employeeReportResult.getResignationTime());
//            }
//        }
//        //3.完成下载
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        wb.write(os);
//        new DownloadUtils().download(os,response,month+"人事报表.xlsx");
        //1、获取数据
        //模板打印数据较小规模方式
        List<EmployeeReportResult> list = userCompanyPersonService.list(month, "1");
        //创建表格 -- 采用模板打印方式
        Resource resource = new ClassPathResource("hrReportTemplate.xlsx");
        FileInputStream fis = new FileInputStream(resource.getFile());
        new ExcelExportUtil(EmployeeReportResult.class, 2, 2)
                .export(response, fis, list, month + " 人事报表.xlsx");
    }

    /**
     * 历史归档详情列表
     */
    @GetMapping("/archives/{month}")
    public Result archives(@PathVariable("month") String month, @RequestParam("type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @PutMapping("/archives/{month}")
    public Result saveArchives(@PathVariable("month") String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @GetMapping("/archives")
    public Result findArchives(@RequestParam("pagesize") Integer pagesize, @RequestParam("page") Integer page, @RequestParam("year") String year) throws Exception {
        QueryWrapper<EmployeeArchive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", companyId);
        queryWrapper.like("month", year);
        IPage<EmployeeArchive> searchPage = new Page<>(page, pagesize);
        IPage<EmployeeArchive> archivesPage = archiveService.page(searchPage, queryWrapper);
        PageResult<EmployeeArchive> pageResult = new PageResult(archivesPage.getTotal(), archivesPage.getRecords());
        return new Result(ResultCode.SUCCESS, pageResult);
    }
}
