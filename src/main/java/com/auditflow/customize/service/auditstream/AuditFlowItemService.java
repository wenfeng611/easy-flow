package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.context.UserInfoThreadLocalContext;
import com.auditflow.customize.entity.AuditFlowItemEntity;
import com.auditflow.customize.factory.FlowFactory;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditFlowItemSearchModel;
import com.auditflow.customize.mapper.AuditFlowItemMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/23 18:05
 */

@Slf4j
@Service
public class AuditFlowItemService {

    @Resource
    private AuditFlowItemMapper auditFlowItemMapper;

    public ApiResponse queryPage(AuditFlowItemSearchModel searchModel){
        PageHelper.startPage(searchModel.getPagenum(), searchModel.getPagesize());
        List<AuditFlowItemEntity> list =  auditFlowItemMapper.selectFlowItems(searchModel);
        PageInfo<AuditFlowItemEntity> pageInfo = new PageInfo(list);

        for (AuditFlowItemEntity item : list) {
            if(StringUtils.isNotBlank(item.getUserIds()) && Arrays.asList(item.getUserIds().split(",")).contains(UserInfoThreadLocalContext.user().getUuid())){
                    item.setAuditPerm(true);
            }else{
                item.setAuditPerm(false);
            }
        }

        return new ApiResponse(list, (int)pageInfo.getTotal());
    }

    public ApiResponse restart(int businessId,String sceneCode){
        //判断场景
        String username = UserInfoThreadLocalContext.user().getUsername();
        AuditFlow flow = FlowFactory.createFlow(sceneCode,username);
        Pair<Boolean,String> result = flow.restartFlow(businessId);
        if(!result.getValue0()){
            return new ApiResponse.Fail(null,400005,result.getValue1());
        }
        return ApiResponse.success(null);
    }


}
