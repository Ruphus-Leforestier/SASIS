package warning_model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
* @author: Ruphus
* Created on 07.08.2019 at 21:35
* */

public class WarningManager {
    private SimpleDateFormat sdf_months = new SimpleDateFormat("yyyy-MM"); // format to plot values monthly
    private SimpleDateFormat sdf_days = new SimpleDateFormat("yyyy-MM-dd"); // format to plot values daily
    private SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm:ss");
    private Date date = new Date();

    public WarningManager(){}

    //get push up object grouped by days
    public List<Warning> groupDaily(List<Warning> warnings){
        final Comparator<Warning> warningComparator =
                Comparator.comparingDouble(Warning::getValue); //get the push-up object that have the greatest value "max" (only one value per day)

        for (Warning warning : warnings) {// for each push-up object, only the id, the max-value and the datetime as"long" are stored in the database
            date.setTime(warning.getDatetime()); // after getting the data stored in the database as a list, the time and date of each object in that list will be formatted
            warning.setDate(sdf_days.format(date));
            warning.setTime(sdf_time.format(date));
        }

        List<Warning> newList = // The elements in the list are grouped (with the Java Stream API) to a new list
                warnings.stream()
                        .collect(Collectors.groupingBy(Warning::getDate,
                                Collectors.maxBy(warningComparator)))
                        .values().stream().map(Optional::get)
                        .collect(Collectors.toList());

        Log.d(getClass().getSimpleName(),"Size: "+ newList.size());
        for (Warning p: newList) {
            Log.d(getClass().getSimpleName(), "ID: " +p.get_id() +"   Value:" + p.getValue() + "Date: "+ p.getDate()+ "   Time: "+p.getTime()+"--> datetime: " + p.getDatetime());
        }
        return newList;
    }

    // the process is same as in the method "groupByMaxofDay"
    public List<Warning> groupMonthly(List<Warning> warnings){
        final Comparator<Warning> pushupComparator =
                (p1, p2) -> Double.compare(p1.getValue(), p2.getValue());
        for (Warning warning : warnings) {
            warning.setDate(sdf_months.format(date));
            warning.setTime(sdf_time.format(date));
        }

        List<Warning> newList = warnings.stream()
                .collect(Collectors.groupingBy(Warning::getDate,
                        Collectors.maxBy(pushupComparator)))
                .values().stream().map(Optional::get)
                .collect(Collectors.toList());

        for (Warning p: newList) {
            Log.d(getClass().getSimpleName(), "ID: " +p.get_id() +"   Value:" + p.getValue() + "Date: "+ p.getDate()+ "   Time: "+p.getTime()+"--> datetime: " + p.getDatetime());
        }
        return newList;
    }

    public List<Warning> reorderedDaily(List<Warning> list){

        List<Warning> reorderedList  = groupDaily(list);
        final Comparator<Warning> datetimeComparator = //get Max of Object
                (p1, p2) -> Long.compare(p1.getDatetime(), p2.getDatetime());

        Log.d(getClass().getSimpleName(),"size reordered List: " + reorderedList.size());
        List<Warning> l = reorderedList.stream()
                .sorted(datetimeComparator)
                .collect(Collectors.toList());
        for (Warning p: l){
            Log.d(getClass().getSimpleName(),"ID : " +p.get_id() + "   Value: "+p.getValue()
                    +"   Time: "+p.getTime()+"   Date: "+p.getDate()+"   Datetime: "+p.getDatetime());
        }
        return l;
    }

    public List<Warning> reorderedMonthly(List<Warning> list){
        List<Warning> reorderedList  = groupMonthly(list);
        final Comparator<Warning> datetimeComparator =
                (p1, p2) -> Long.compare(p1.getDatetime(), p2.getDatetime());

        Log.d(getClass().getSimpleName(),"size reordered List: " + reorderedList.size());
        List<Warning> l = reorderedList.stream()
                .sorted(datetimeComparator)
                .collect(Collectors.toList());
        for (Warning p: l){
            Log.d(getClass().getSimpleName(),"ID : " +p.get_id() + "   Value: "+p.getValue()
                    +"   Time: "+p.getTime()+"   Date: "+p.getDate()+"   Datetime: "+p.getDatetime());
        }
        return l;
    }

    // the best performance is displayed only when the stop-button is clicked.
    // the method "getBestperformance" show the object with the greatest value on the screen
    // http://www.techiedelight.com/find-maximum-minimum-custom-objects-java/
    public Warning getHighestUse( List<Warning> list){
        Warning war = list
                .stream()
                .max(Comparator.comparing(Warning::getValue))
                .get();
        war.setTime(sdf_time.format(war.getDatetime()));
        war.setDate(sdf_days.format(war.getDatetime()));
        Log.d(getClass().getSimpleName(), "Highest use::\n" +
                "ID: "+ war.get_id()+"\nValue: " + war.getValue()+
                "\nDate: " + war.getDate()+
                "\nTime: "+ war.getTime()+
                "\nDatetime: "+war.getDatetime());

        return war;
    }
}
