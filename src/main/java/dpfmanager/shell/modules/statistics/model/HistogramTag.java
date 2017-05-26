package dpfmanager.shell.modules.statistics.model;

import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.easyinnova.tiff.model.TagValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class HistogramTag {

  public TagValue tv;
  public Integer main;
  public Integer thumb;

  public Map<String, Integer> possibleValues;

  public HistogramTag(ReportTag tag){
    possibleValues = new HashMap<>();
    tv = tag.tv;
    main = 0;
    thumb = 0;
  }

  public void increaseMainCount(){
    main++;
  }

  public void increaseThumbCount(){
    thumb++;
  }

  public TagValue getValue() {
    return tv;
  }

  public Integer getMainCount() {
    return main;
  }

  public Integer getThumbCount() {
    return thumb;
  }

  public Map<String, Integer> getPossibleValues() {
    return possibleValues;
  }
}
