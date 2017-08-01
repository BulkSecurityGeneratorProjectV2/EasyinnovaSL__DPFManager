/**
 * <h1>StatisticsIso.java</h1> <p> This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version; or, at your
 * choice, under the terms of the Mozilla Public License, v. 2.0. SPDX GPL-3.0+ or MPL-2.0+. </p>
 * <p> This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License and the Mozilla Public License for more details. </p> <p> You should
 * have received a copy of the GNU General Public License and the Mozilla Public License along with
 * this program. If not, see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>
 * and at <a href="http://mozilla.org/MPL/2.0">http://mozilla.org/MPL/2.0</a> . </p> <p> NB: for the
 * © statement, include Easy Innova SL or other company/Person contributing the code. </p> <p> ©
 * 2015 Easy Innova, SL </p>
 */

package dpfmanager.shell.modules.statistics.model;

import dpfmanager.conformancechecker.tiff.reporting.ReportTag;

import com.easyinnova.tiff.model.TagValue;

/**
 * Created by Adrià Llorens on 23/05/2017.
 */
public class StatisticsIso {

  public String iso;
  public String id;
  public Integer count;
  public Integer errors;
  public Integer warnings;
  public Integer passed;

  private StatisticsIsoErrors isoErrors;

  public StatisticsIso(String i, String d){
    iso = i;
    id = d;
    errors = 0;
    warnings = 0;
    passed = 0;
    count = 0;
    isoErrors = new StatisticsIsoErrors(iso, id);
  }

  public StatisticsIsoErrors getIsoErrors() {
    return isoErrors;
  }
}
