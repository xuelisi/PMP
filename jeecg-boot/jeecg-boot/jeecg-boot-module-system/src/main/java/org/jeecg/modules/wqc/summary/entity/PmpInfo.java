package org.jeecg.modules.wqc.summary.entity;

import lombok.Data;
import org.jeecg.common.util.DateUtils;

import java.util.List;

@Data
public class PmpInfo {

    private String taskID;

    private String taskName;

    private String projectID;

    private String projectName;
}
