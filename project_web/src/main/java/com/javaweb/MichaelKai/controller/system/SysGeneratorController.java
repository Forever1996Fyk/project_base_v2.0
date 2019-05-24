package com.javaweb.MichaelKai.controller.system;

import com.github.pagehelper.PageInfo;
import com.javaweb.MichaelKai.common.enums.ResultEnum;
import com.javaweb.MichaelKai.common.vo.PageResult;
import com.javaweb.MichaelKai.common.vo.Result;
import com.javaweb.MichaelKai.service.SysGeneratorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * @author earl
 * @date 2017-05-18 10:59
 */
@Controller
@RequestMapping("/api/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;


	/**
	 * 获取所有数据库表
	 * @param page
	 * @param limit
	 * @param map
	 * @return
	 */
	@GetMapping("/getDBTables")
	@ResponseBody
	public Result getTables(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
						   @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
						   @RequestParam Map<String, Object> map) {
		PageInfo<Map<String, Object>> pageList = sysGeneratorService.pageList(page, limit, map) ;
		return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), new PageResult<>(pageList.getTotal(), pageList.getList()));
	}

	/**
	 * 根据表名获取所有数据库表字段
	 * @param tableName
	 * @return
	 */
	@GetMapping("/getDBColumns")
	@ResponseBody
	public Result getColumns(String tableName) {
		List<Map<String, String>> list = sysGeneratorService.queryColumns(tableName);
		return new Result(true, ResultEnum.SUCCESS.getValue(), ResultEnum.SUCCESS.getMessage(), list);
	}


	/**
	 * 生成代码
	 */
	@RequestMapping(value = "/code/{tables}",method = {RequestMethod.GET, RequestMethod.POST})
	public void code(@PathVariable("tables") String[] tables, HttpServletRequest request, HttpServletResponse response) throws IOException{
		/*String[] tableNames = StringUtils.split(tables,",");*/
		byte[] data = sysGeneratorService.generatorCode(tables);
		response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
	}
}
