
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 旅游路线
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/lvyouluxian")
public class LvyouluxianController {
    private static final Logger logger = LoggerFactory.getLogger(LvyouluxianController.class);

    @Autowired
    private LvyouluxianService lvyouluxianService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service

    @Autowired
    private YonghuService yonghuService;


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = lvyouluxianService.queryPage(params);

        //字典表数据转换
        List<LvyouluxianView> list =(List<LvyouluxianView>)page.getList();
        for(LvyouluxianView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        LvyouluxianEntity lvyouluxian = lvyouluxianService.selectById(id);
        if(lvyouluxian !=null){
            //entity转view
            LvyouluxianView view = new LvyouluxianView();
            BeanUtils.copyProperties( lvyouluxian , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody LvyouluxianEntity lvyouluxian, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,lvyouluxian:{}",this.getClass().getName(),lvyouluxian.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<LvyouluxianEntity> queryWrapper = new EntityWrapper<LvyouluxianEntity>()
            .eq("lvyouluxian_uuid_number", lvyouluxian.getLvyouluxianUuidNumber())
            .eq("lvyouluxian_name", lvyouluxian.getLvyouluxianName())
            .eq("lvyouluxian_types", lvyouluxian.getLvyouluxianTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        LvyouluxianEntity lvyouluxianEntity = lvyouluxianService.selectOne(queryWrapper);
        if(lvyouluxianEntity==null){
            lvyouluxian.setInsertTime(new Date());
            lvyouluxian.setCreateTime(new Date());
            lvyouluxianService.insert(lvyouluxian);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody LvyouluxianEntity lvyouluxian, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,lvyouluxian:{}",this.getClass().getName(),lvyouluxian.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        //根据字段查询是否有相同数据
        Wrapper<LvyouluxianEntity> queryWrapper = new EntityWrapper<LvyouluxianEntity>()
            .notIn("id",lvyouluxian.getId())
            .andNew()
            .eq("lvyouluxian_uuid_number", lvyouluxian.getLvyouluxianUuidNumber())
            .eq("lvyouluxian_name", lvyouluxian.getLvyouluxianName())
            .eq("lvyouluxian_types", lvyouluxian.getLvyouluxianTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        LvyouluxianEntity lvyouluxianEntity = lvyouluxianService.selectOne(queryWrapper);
        if("".equals(lvyouluxian.getLvyouluxianPhoto()) || "null".equals(lvyouluxian.getLvyouluxianPhoto())){
                lvyouluxian.setLvyouluxianPhoto(null);
        }
        if(lvyouluxianEntity==null){
            lvyouluxianService.updateById(lvyouluxian);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        lvyouluxianService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        try {
            List<LvyouluxianEntity> lvyouluxianList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            LvyouluxianEntity lvyouluxianEntity = new LvyouluxianEntity();
//                            lvyouluxianEntity.setLvyouluxianUuidNumber(data.get(0));                    //路线编号 要改的
//                            lvyouluxianEntity.setLvyouluxianName(data.get(0));                    //路线标题 要改的
//                            lvyouluxianEntity.setLvyouluxianTypes(Integer.valueOf(data.get(0)));   //路线类型 要改的
//                            lvyouluxianEntity.setLvyouluxianPhoto("");//照片
//                            lvyouluxianEntity.setLvyouluxianContent("");//照片
//                            lvyouluxianEntity.setInsertTime(date);//时间
//                            lvyouluxianEntity.setCreateTime(date);//时间
                            lvyouluxianList.add(lvyouluxianEntity);


                            //把要查询是否重复的字段放入map中
                                //路线编号
                                if(seachFields.containsKey("lvyouluxianUuidNumber")){
                                    List<String> lvyouluxianUuidNumber = seachFields.get("lvyouluxianUuidNumber");
                                    lvyouluxianUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> lvyouluxianUuidNumber = new ArrayList<>();
                                    lvyouluxianUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("lvyouluxianUuidNumber",lvyouluxianUuidNumber);
                                }
                        }

                        //查询是否重复
                         //路线编号
                        List<LvyouluxianEntity> lvyouluxianEntities_lvyouluxianUuidNumber = lvyouluxianService.selectList(new EntityWrapper<LvyouluxianEntity>().in("lvyouluxian_uuid_number", seachFields.get("lvyouluxianUuidNumber")));
                        if(lvyouluxianEntities_lvyouluxianUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(LvyouluxianEntity s:lvyouluxianEntities_lvyouluxianUuidNumber){
                                repeatFields.add(s.getLvyouluxianUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [路线编号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        lvyouluxianService.insertBatch(lvyouluxianList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }





    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        // 没有指定排序字段就默认id倒序
        if(StringUtil.isEmpty(String.valueOf(params.get("orderBy")))){
            params.put("orderBy","id");
        }
        PageUtils page = lvyouluxianService.queryPage(params);

        //字典表数据转换
        List<LvyouluxianView> list =(List<LvyouluxianView>)page.getList();
        for(LvyouluxianView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段
        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        LvyouluxianEntity lvyouluxian = lvyouluxianService.selectById(id);
            if(lvyouluxian !=null){


                //entity转view
                LvyouluxianView view = new LvyouluxianView();
                BeanUtils.copyProperties( lvyouluxian , view );//把实体数据重构到view中

                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody LvyouluxianEntity lvyouluxian, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,lvyouluxian:{}",this.getClass().getName(),lvyouluxian.toString());
        Wrapper<LvyouluxianEntity> queryWrapper = new EntityWrapper<LvyouluxianEntity>()
            .eq("lvyouluxian_uuid_number", lvyouluxian.getLvyouluxianUuidNumber())
            .eq("lvyouluxian_name", lvyouluxian.getLvyouluxianName())
            .eq("lvyouluxian_types", lvyouluxian.getLvyouluxianTypes())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        LvyouluxianEntity lvyouluxianEntity = lvyouluxianService.selectOne(queryWrapper);
        if(lvyouluxianEntity==null){
            lvyouluxian.setInsertTime(new Date());
            lvyouluxian.setCreateTime(new Date());
        lvyouluxianService.insert(lvyouluxian);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }


}
