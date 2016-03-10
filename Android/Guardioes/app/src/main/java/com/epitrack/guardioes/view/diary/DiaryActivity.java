package com.epitrack.guardioes.view.diary;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.RequestListener;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.Logger;
import com.epitrack.guardioes.utility.MySelectorDecoratorBad;
import com.epitrack.guardioes.utility.MySelectorDecoratorEquals;
import com.epitrack.guardioes.utility.MySelectorDecoratorGood;
import com.epitrack.guardioes.utility.MySelectorDecoratorOnlyBad;
import com.epitrack.guardioes.utility.MySelectorDecoratorOnlyGood;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.survey.ParentListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.gms.analytics.HitBuilders;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * @author Igor Morais
 */
public class DiaryActivity extends BaseAppCompatActivity implements ParentListener, OnDateSelectedListener, OnMonthChangedListener {

    private static final String TAG = DiaryActivity.class.getSimpleName();

    @Bind(R.id.text_view_participation)
    TextView textViewParticipation;

    @Bind(R.id.text_view_good_percentage)
    TextView textViewGoodPercentage;

    @Bind(R.id.text_view_good_report)
    TextView textViewGoodReport;

    @Bind(R.id.text_view_bad_percentage)
    TextView textViewBadPercentage;

    @Bind(R.id.text_view_bad_report)
    TextView textViewBadReport;

    @Bind(R.id.text_view_good_report_2)
    TextView textViewGoodReportDetail;

    @Bind(R.id.text_view_bad_report_2)
    TextView textViewBadReportDetail;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.pie_chart_diary)
    PieChart pieChart;

    @Bind(R.id.layout_detail_good)
    RelativeLayout layoutDetailGood;

    @Bind(R.id.layout_detail_bad)
    RelativeLayout layoutDetailBad;

    @Bind(R.id.calendarView)
    MaterialCalendarView materialCalendarView;

    @Bind(R.id.text_view_total_dia)
    TextView textViewTotalDia;

    @Bind(R.id.frequency_report)
    TextView textViewFrequencyReport;

    @Bind(R.id.line_chart)
    GraphView lineChart;

    private double totalCount = 0;
    private double goodCount = 0;
    private double badCount = 0;
    private double goodPercent = 0;
    private double badPercent = 0;

    private double totalCountDetail = 0;
    private double goodCountDetail = 0;
    private double badCountDetail = 0;
    private double goodPercentDetail = 0;
    private double badPercentDetail = 0;

    private String idSelectedUser = "";
    SingleUser singleUser = SingleUser.getInstance();
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private ArrayList<Date> dateArrayList = new ArrayList<>();
    private ArrayList<Integer> daysGood = null;
    private ArrayList<Integer> daysBad = null;
    private ArrayList<Integer> daysZero = null;
    private ArrayList<Integer> daysOnlyGood = null;
    private ArrayList<Integer> daysOnlyBad = null;
    private ArrayList<Integer> daysEquals = null;
    private ArrayList<Date> daysGoodAndBad = null;

    private DiaryActivity context;
    private CalendarDay calendarDay;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.diary);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        context = this;

        final List<User> parentList = new ArrayList<>();

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setUrl(Requester.API_URL + "user/household/" + singleUser.getId());
        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        parentList.add(new User(R.drawable.image_avatar_small_8, singleUser.getNick(),
                singleUser.getEmail(), singleUser.getId(),
                singleUser.getDob(), singleUser.getRace(),
                singleUser.getGender(), singleUser.getPicture()));

        recyclerView.setAdapter(new MemberAdapter(DiaryActivity.this, DiaryActivity.this, parentList));

        simpleRequester.execute();

        simpleRequester.setListener(new RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final String result) {

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.get("error").toString() == "false") {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {

                            JSONObject jsonObjectHousehold;

                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObjectHousehold = jsonArray.getJSONObject(i);
                                parentList.add(new User(R.drawable.image_avatar_small_8, jsonObjectHousehold.get("nick").toString(),
                                        "", jsonObjectHousehold.get("id").toString(),
                                        jsonObjectHousehold.get("dob").toString(), jsonObjectHousehold.get("race").toString(),
                                        jsonObjectHousehold.get("gender").toString(), jsonObjectHousehold.get("picture").toString()));
                            }

                            recyclerView.setAdapter(new MemberAdapter(DiaryActivity.this, DiaryActivity.this, parentList));
                        }
                    }

                } catch (JSONException e) {
                    Logger.logError(TAG, e.getMessage());
                }

                setDataLineChart();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Diary of Health Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    private void setupView(String idHouseHold) {

        SimpleRequester simpleRequester = new SimpleRequester();

        if (idHouseHold == singleUser.getId()) {
            simpleRequester.setUrl(Requester.API_URL + "user/survey/summary");
        } else if (idHouseHold == null) {
            simpleRequester.setUrl(Requester.API_URL + "user/survey/summary");
        } else {
            simpleRequester.setUrl(Requester.API_URL + "household/survey/summary?household_id=" + idHouseHold);
        }

        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        simpleRequester.execute();

        simpleRequester.setListener(new RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final String result) {

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObjectSympton = jsonObject.getJSONObject("data");

                    goodCount = Integer.parseInt(jsonObjectSympton.get("no_symptom").toString());
                    badCount = Integer.parseInt(jsonObjectSympton.get("symptom").toString());
                    totalCount = Integer.parseInt(jsonObjectSympton.get("total").toString());

                    textViewParticipation.setText((int) totalCount + " Participações");

                    if (totalCount == 0) {
                        goodPercent = 0;
                    } else {
                        goodPercent = goodCount / totalCount;
                    }

                    String htmlStringGood = "<b>" + (int) (goodPercent * 100) + "%</b> Bem";
                    textViewGoodPercentage.setText(Html.fromHtml(htmlStringGood));
                    //textViewGoodPercentage.setText((int) (goodPercent * 100) + "% Bem");
                    textViewGoodReport.setText((int) goodCount + " Relatórios");

                    if (totalCount == 0) {
                        badPercent = 0;
                    } else {
                        badPercent = badCount / totalCount;
                    }

                    String htmlStringBad = "<b>" + (int) (badPercent * 100) + "%</b> Mal";
                    textViewBadPercentage.setText(Html.fromHtml(htmlStringBad));
                    //textViewBadPercentage.setText((int) (badPercent * 100) + "% Mal");
                    textViewBadReport.setText((int) badCount + " Relatórios");

                    //Pie Char Config
                    pieChart.setUsePercentValues(false);
                    pieChart.setDescription("");
                    pieChart.setDrawCenterText(false);
                    pieChart.setDrawSliceText(false);
                    pieChart.setDrawHoleEnabled(false);
                    pieChart.setHoleColorTransparent(false);
                    pieChart.setHoleRadius(7);
                    pieChart.setTransparentCircleRadius(10);
                    pieChart.setRotationAngle(0);
                    pieChart.setRotationEnabled(false);
                    pieChart.setClickable(false);

                    setData();

                    layoutDetailGood.setVisibility(View.INVISIBLE);
                    layoutDetailBad.setVisibility(View.INVISIBLE);

                    //Calendar Config
                    materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
                    materialCalendarView.setArrowColor(R.color.blue_light);
                    materialCalendarView.setOnDateChangedListener(DiaryActivity.this);
                    materialCalendarView.setOnMonthChangedListener(DiaryActivity.this);
                    materialCalendarView.setWeekDayLabels(new String[]{"D", "S", "T", "Q", "Q", "S", "S"});
                    materialCalendarView.setTitleMonths(new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"});
                    calendarDay = CalendarDay.today();

                    //new AsyncTaskRunner().execute();

                    setTextTotalReport(null);

                } catch (Exception e) {
                    Logger.logError(TAG, e.getMessage());
                }
            }
        });
    }

    private void setTextTotalReport(CalendarDay date) {

        if (date == null) {
            date = CalendarDay.today();
        }

        String today = date.getDay() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
        textViewTotalDia.setText("Total de envios no dia " + today);
    }

    @Override
    public void onParentSelect(String id) {
        idSelectedUser = id;

        new AsyncTaskRunner().executeOnExecutor(Executors.newSingleThreadExecutor());
        materialCalendarView.setSelectedDate(CalendarDay.today());
        onDateChanged(materialCalendarView, CalendarDay.today());

        setDataLineChart();
        setTextTotalReport(null);
        setupView(id);
    }

    private void setData() {

        float[] yData = {(int) (badPercent * 100), (int) (goodPercent * 100)};
        String[] xData = {"Mal", "Bem"};

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++) {
            xVals.add(xData[i]);
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(2);

        int colors[] = {Color.parseColor("#FF0000"), Color.parseColor("#CCCC00")};

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setDrawValues(false);
        data.setHighlightEnabled(false);

        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.getLegend().setEnabled(false);
    }

    private void setDataLineChart() {
        pieChart.getLegend().setEnabled(false);
        textViewFrequencyReport.setText("Frequência de envio de relatório em " + CalendarDay.today().getYear());

        final Map<Integer, Double> mapTotalMonth = new HashMap<>();
        final Map<Integer, Double> mapTotalMonthTemp = new HashMap<>();
        mapTotalMonthTemp.put(1, 0.0);
        mapTotalMonthTemp.put(2, 0.0);
        mapTotalMonthTemp.put(3, 0.0);
        mapTotalMonthTemp.put(4, 0.0);
        mapTotalMonthTemp.put(5, 0.0);
        mapTotalMonthTemp.put(6, 0.0);
        mapTotalMonthTemp.put(7, 0.0);
        mapTotalMonthTemp.put(8, 0.0);
        mapTotalMonthTemp.put(9, 0.0);
        mapTotalMonthTemp.put(10, 0.0);
        mapTotalMonthTemp.put(11, 0.0);
        mapTotalMonthTemp.put(12, 0.0);

        SimpleRequester simpleRequester = new SimpleRequester();

        if (idSelectedUser == singleUser.getId()) {
            simpleRequester.setUrl(Requester.API_URL + "user/calendar/year?year=" + CalendarDay.today().getYear());
        } else if (idSelectedUser.equals("")) {
            simpleRequester.setUrl(Requester.API_URL + "user/calendar/year?year=" + CalendarDay.today().getYear());
        } else {
            simpleRequester.setUrl(Requester.API_URL + "household/calendar/year?year=" + CalendarDay.today().getYear() + "&household_id=" + idSelectedUser);
        }

        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        simpleRequester.execute();

        simpleRequester.setListener(new RequestListener<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.get("error").toString() == "true") {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectTotalMonth = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectDetail = jsonObjectTotalMonth.getJSONObject("_id");

                            int month = Integer.parseInt(jsonObjectDetail.get("month").toString());

                            for (int j = 1; j <= mapTotalMonthTemp.size(); j++) {

                                if (j == month) {
                                    mapTotalMonthTemp.put(j, Double.parseDouble(jsonObjectTotalMonth.get("percent").toString()));
                                }
                            }
                        }

                        lineChart.removeAllSeries();
                        lineChart.getGridLabelRenderer().setTextSize(18);
                        lineChart.getGridLabelRenderer().reloadStyles();

                        //Line Chart
                        mapTotalMonth.put(1, mapTotalMonthTemp.get(1));
                        mapTotalMonth.put(2, mapTotalMonthTemp.get(2));
                        mapTotalMonth.put(3, mapTotalMonthTemp.get(3));
                        mapTotalMonth.put(4, mapTotalMonthTemp.get(4));
                        mapTotalMonth.put(5, mapTotalMonthTemp.get(5));
                        mapTotalMonth.put(6, mapTotalMonthTemp.get(6));
                        mapTotalMonth.put(7, mapTotalMonthTemp.get(7));
                        mapTotalMonth.put(8, mapTotalMonthTemp.get(8));
                        mapTotalMonth.put(9, mapTotalMonthTemp.get(9));
                        mapTotalMonth.put(10, mapTotalMonthTemp.get(10));
                        mapTotalMonth.put(11, mapTotalMonthTemp.get(11));
                        mapTotalMonth.put(12, mapTotalMonthTemp.get(12));

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

                        for (int k = 1; k <= mapTotalMonth.size(); k++) {
                            series.appendData(new DataPoint(k, mapTotalMonth.get(k)), true, 100);
                        }

                        series.setDrawDataPoints(true);
                        series.setBackgroundColor(R.color.blue_dark);
                        series.setDataPointsRadius(5);

                        series.setOnDataPointTapListener(new OnDataPointTapListener() {

                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                Toast.makeText(getApplicationContext(), dataPoint.getY() + "% da frequência no envio de relatórios.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(lineChart);
                        staticLabelsFormatter.setHorizontalLabels(new String[]{"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"});
                        staticLabelsFormatter.setVerticalLabels(new String[]{"0%", "25%", "50%", "75%", "100%"});

                        lineChart.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                        lineChart.getGridLabelRenderer().setVerticalLabelsColor(R.color.grey_light);
                        lineChart.getGridLabelRenderer().setHorizontalLabelsColor(R.color.grey_light);
                        lineChart.setTitleColor(R.color.grey_light);
                        lineChart.setTitleTextSize(12f);
                        lineChart.addSeries(series);
                        Viewport viewport = lineChart.getViewport();
                        viewport.setYAxisBoundsManual(true);
                        viewport.setMinY(0);
                        viewport.setMaxY(100);
                        viewport.setScrollable(false);
                    }

                } catch (Exception e) {
                    Logger.logError(TAG, e.getMessage());
                }

                setupView(null);
            }

        });
    }


    public void onDateChanged(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date) {
        onDateSelected(widget, date, true);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay date) {
        calendarDay = date;
        new AsyncTaskRunner().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        widget.invalidateDecorators();

        if (layoutDetailGood.getVisibility() == View.INVISIBLE) {
            layoutDetailGood.setVisibility(View.VISIBLE);
            layoutDetailBad.setVisibility(View.VISIBLE);
        }

        if (date != null) {

            if (daysGoodAndBad.size() > 0) {
                for (int i = 0; i < daysGoodAndBad.size(); i++) {
                    if (daysGoodAndBad.get(i).equals(date.getDate())) {
                        widget.setDateSelected(daysGoodAndBad.get(i), true);
                    }
                }
            }

            setTextTotalReport(date);

            SimpleRequester simpleRequester = new SimpleRequester();

            if (idSelectedUser == singleUser.getId()) {
                simpleRequester.setUrl(Requester.API_URL + "user/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
            } else if (idSelectedUser.equals("")) {
                simpleRequester.setUrl(Requester.API_URL + "user/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
            } else {
                simpleRequester.setUrl(Requester.API_URL + "household/calendar/day?day=" + date.getDay() + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear() + "&household_id=" + idSelectedUser);
            }

            simpleRequester.setJsonObject(null);
            simpleRequester.setMethod(Method.GET);

            String jsonStr;
            goodCountDetail = 0;
            badCountDetail = 0;

            try {
                jsonStr = simpleRequester.execute(simpleRequester).get();

                JSONObject jsonObject = new JSONObject(jsonStr);

                if (jsonObject.get("error").toString() == "true") {
                    Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                } else {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectSymptom = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectDetail = jsonObjectSymptom.getJSONObject("_id");

                        if (jsonObjectDetail.get("no_symptom").toString().equals("N")) {

                            badCountDetail = Double.parseDouble(jsonObjectSymptom.get("count").toString());

                        } else if (jsonObjectDetail.get("no_symptom").toString().equals("Y")) {

                            goodCountDetail = Double.parseDouble(jsonObjectSymptom.get("count").toString());
                        }
                    }

                    totalCountDetail = badCountDetail + goodCountDetail;

                    if (badCountDetail == 0) {
                        textViewBadReportDetail.setText("     0 Relatórios \"Estou Mal\"");
                    } else {
                        badPercentDetail = ((badCountDetail / totalCountDetail) * 100);
                        textViewBadReportDetail.setText("     " + (int) badCountDetail + " Relatórios \"Estou Mal\"");
                    }

                    if (goodCountDetail == 0) {
                        textViewGoodReportDetail.setText("     0 Relatórios \"Estou Bem\"");
                    } else {
                        goodPercentDetail = ((goodCountDetail / totalCountDetail) * 100);
                        textViewGoodReportDetail.setText("     " + (int) goodCountDetail + " Relatórios \"Estou Bem\"");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void countTotalGoodAndBad(MaterialCalendarView materialCalendarView, CalendarDay date) {

        daysGood = new ArrayList<>();
        daysBad = new ArrayList<>();
        daysZero = new ArrayList<>();
        daysGoodAndBad = new ArrayList<>();
        daysOnlyGood = new ArrayList<>();
        daysOnlyBad = new ArrayList<>();
        daysEquals = new ArrayList<>();

        for (int j = 1; j <= 31; j++) {

            SimpleRequester simpleRequester = new SimpleRequester();

            if (idSelectedUser.equals(singleUser.getId())) {
                simpleRequester.setUrl(Requester.API_URL + "user/calendar/day?day=" + j + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
            } else if (idSelectedUser.equals("")) {
                simpleRequester.setUrl(Requester.API_URL + "user/calendar/day?day=" + j + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear());
            } else {
                simpleRequester.setUrl(Requester.API_URL + "household/calendar/day?day=" + j + "&month=" + (date.getMonth() + 1) + "&year=" + date.getYear() + "&household_id=" + idSelectedUser);
            }

            simpleRequester.setJsonObject(null);
            simpleRequester.setMethod(Method.GET);

            String jsonStr;
            int goodCountTotal = 0;
            int badCountTotal = 0;

            try {
                jsonStr = simpleRequester.execute(simpleRequester).get();

                JSONObject jsonObject = new JSONObject(jsonStr);

                if (jsonObject.get("error").toString().equals("false")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectSymptom = jsonArray.getJSONObject(i);
                        JSONObject jsonObjectDetail = jsonObjectSymptom.getJSONObject("_id");

                        if (jsonObjectDetail.get("no_symptom").toString().equals("N")) {

                            badCountTotal = Integer.parseInt(jsonObjectSymptom.get("count").toString());

                        } else if (jsonObjectDetail.get("no_symptom").toString().equals("Y")) {

                            goodCountTotal = Integer.parseInt(jsonObjectSymptom.get("count").toString());
                        }
                    }
                    if (goodCountTotal == 0 && badCountTotal == 0) {
                        daysZero.add(j);
                    } else {

                        if (goodCountTotal == 0 && badCountTotal > 0) {
                            daysOnlyBad.add(j);
                        } else if (goodCountTotal > 0 && badCountTotal == 0) {
                            daysOnlyGood.add(j);
                        } else if (goodCountTotal > badCountTotal) {
                            daysGood.add(j);
                        } else if (goodCountTotal < badCountTotal) {
                            daysBad.add(j);
                        } else if (goodCountTotal == badCountTotal) {
                            daysEquals.add(j);
                        }

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date dateFormated = format.parse(date.getYear() + "-" + (date.getMonth() + 1) + "-" + j);
                        daysGoodAndBad.add(dateFormated);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            countTotalGoodAndBad(materialCalendarView, calendarDay);
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            // execution of result of Long time consuming operation
            onDateChanged(materialCalendarView, calendarDay);
            materialCalendarView.removeDecorators();
            materialCalendarView.addDecorators(new MySelectorDecoratorGood(context, daysGood), new MySelectorDecoratorBad(context, daysBad), new DayDisableDecorator(daysZero),
                    new MySelectorDecoratorOnlyGood(context, daysOnlyGood), new MySelectorDecoratorOnlyBad(context, daysOnlyBad), new MySelectorDecoratorEquals(context, daysEquals));
        }

        @Override
        protected void onPreExecute() {

        }
    }
}

class DayDisableDecorator implements DayViewDecorator {

    private ArrayList<Integer> days;

    public DayDisableDecorator(ArrayList<Integer> days) {
        this.days = days;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (days.size() > 0) {
            for (int i = 0; i < days.size(); i++) {
                if (days.get(i) == day.getDay()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}

