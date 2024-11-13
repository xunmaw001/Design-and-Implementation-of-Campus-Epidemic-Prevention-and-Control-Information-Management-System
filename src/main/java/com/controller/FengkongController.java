
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
 * 隔离信息
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/fengkong")
public class FengkongController {
    private static final Logger logger = LoggerFactory.getLogger(FengkongController.class);

    private static final String TABLE_NAME = "fengkong";

    @Autowired
    private FengkongService fengkongService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private ChatService chatService;//疫情咨询
    @Autowired
    private DakaService dakaService;//疫情数据
    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private ForumService forumService;//交流论坛
    @Autowired
    private GonggaoService gonggaoService;//通知公告
    @Autowired
    private JiaoyuService jiaoyuService;//防疫宣传
    @Autowired
    private WangfanYuyueService wangfanYuyueService;//上报信息
    @Autowired
    private YimiaoService yimiaoService;//疫苗
    @Autowired
    private YimiaoYuyueService yimiaoYuyueService;//疫苗接种
    @Autowired
    private YonghuService yonghuService;//用户
    @Autowired
    private UsersService usersService;//管理员


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
        params.put("fengkongDeleteStart",1);params.put("fengkongDeleteEnd",1);
        CommonUtil.checkMap(params);
        PageUtils page = fengkongService.queryPage(params);

        //字典表数据转换
        List<FengkongView> list =(List<FengkongView>)page.getList();
        for(FengkongView c:list){
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
        FengkongEntity fengkong = fengkongService.selectById(id);
        if(fengkong !=null){
            //entity转view
            FengkongView view = new FengkongView();
            BeanUtils.copyProperties( fengkong , view );//把实体数据重构到view中
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
    public R save(@RequestBody FengkongEntity fengkong, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,fengkong:{}",this.getClass().getName(),fengkong.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<FengkongEntity> queryWrapper = new EntityWrapper<FengkongEntity>()
            .eq("fengkong_name", fengkong.getFengkongName())
            .eq("fengkong_didian_types", fengkong.getFengkongDidianTypes())
            .eq("fengkong_types", fengkong.getFengkongTypes())
            .eq("fengkong_delete", 1)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        FengkongEntity fengkongEntity = fengkongService.selectOne(queryWrapper);
        if(fengkongEntity==null){
            fengkong.setFengkongDelete(1);
            fengkong.setInsertTime(new Date());
            fengkong.setCreateTime(new Date());
            fengkongService.insert(fengkong);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody FengkongEntity fengkong, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,fengkong:{}",this.getClass().getName(),fengkong.toString());
        FengkongEntity oldFengkongEntity = fengkongService.selectById(fengkong.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(fengkong.getFengkongPhoto()) || "null".equals(fengkong.getFengkongPhoto())){
                fengkong.setFengkongPhoto(null);
        }
        if("".equals(fengkong.getFengkongContent()) || "null".equals(fengkong.getFengkongContent())){
                fengkong.setFengkongContent(null);
        }

            fengkongService.updateById(fengkong);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<FengkongEntity> oldFengkongList =fengkongService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        ArrayList<FengkongEntity> list = new ArrayList<>();
        for(Integer id:ids){
            FengkongEntity fengkongEntity = new FengkongEntity();
            fengkongEntity.setId(id);
            fengkongEntity.setFengkongDelete(2);
            list.add(fengkongEntity);
        }
        if(list != null && list.size() >0){
            fengkongService.updateBatchById(list);
        }

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<FengkongEntity> fengkongList = new ArrayList<>();//上传的东西
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
                            FengkongEntity fengkongEntity = new FengkongEntity();
//                            fengkongEntity.setFengkongName(data.get(0));                    //地区名称 要改的
//                            fengkongEntity.setFengkongPhoto("");//详情和图片
//                            fengkongEntity.setFengkongDidianTypes(Integer.valueOf(data.get(0)));   //地区 要改的
//                            fengkongEntity.setFengkongTypes(Integer.valueOf(data.get(0)));   //风险类型 要改的
//                            fengkongEntity.setFengkongContent("");//详情和图片
//                            fengkongEntity.setFengkongDelete(1);//逻辑删除字段
//                            fengkongEntity.setInsertTime(date);//时间
//                            fengkongEntity.setCreateTime(date);//时间
                            fengkongList.add(fengkongEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        fengkongService.insertBatch(fengkongList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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

        CommonUtil.checkMap(params);
        PageUtils page = fengkongService.queryPage(params);

        //字典表数据转换
        List<FengkongView> list =(List<FengkongView>)page.getList();
        for(FengkongView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        FengkongEntity fengkong = fengkongService.selectById(id);
            if(fengkong !=null){


                //entity转view
                FengkongView view = new FengkongView();
                BeanUtils.copyProperties( fengkong , view );//把实体数据重构到view中

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
    public R add(@RequestBody FengkongEntity fengkong, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,fengkong:{}",this.getClass().getName(),fengkong.toString());
        Wrapper<FengkongEntity> queryWrapper = new EntityWrapper<FengkongEntity>()
            .eq("fengkong_name", fengkong.getFengkongName())
            .eq("fengkong_didian_types", fengkong.getFengkongDidianTypes())
            .eq("fengkong_types", fengkong.getFengkongTypes())
            .eq("fengkong_delete", fengkong.getFengkongDelete())
//            .notIn("fengkong_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        FengkongEntity fengkongEntity = fengkongService.selectOne(queryWrapper);
        if(fengkongEntity==null){
            fengkong.setFengkongDelete(1);
            fengkong.setInsertTime(new Date());
            fengkong.setCreateTime(new Date());
        fengkongService.insert(fengkong);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}

