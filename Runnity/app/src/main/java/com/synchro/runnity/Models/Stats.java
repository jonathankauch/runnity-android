package com.synchro.runnity.Models;

/**
 * Created by Shirco on 01/03/2016.
 */
public class Stats {
    private int kms;
    private int nb_runs;
    private int nb_steps;
    private int max_speed;
    private int calories;
    private float average_speed;
    private int total_time_run;
    private int[] runs_by_month;
    private int[] time_run_by_month;
    private float[] distance_by_month;
    private float[] calories_by_month;

    public int getKms() {
        return kms;
    }

    public void setKms(int kms) {
        this.kms = kms;
    }

    public int getNb_runs() {
        return nb_runs;
    }

    public void setNb_runs(int nb_runs) {
        this.nb_runs = nb_runs;
    }

    public int getNb_steps() {
        return nb_steps;
    }

    public void setNb_steps(int nb_steps) {
        this.nb_steps = nb_steps;
    }

    public int getMax_speed() {
        return max_speed;
    }

    public void setMax_speed(int max_speed) {
        this.max_speed = max_speed;
    }

    public float getAverage_speed() {
        return average_speed;
    }

    public void setAverage_speed(float average_speed) {
        this.average_speed = average_speed;
    }

    public int getTotal_time_run() {
        return total_time_run;
    }

    public void setTotal_time_run(int total_time_run) {
        this.total_time_run = total_time_run;
    }

    public int[] getRuns_by_month() {
        return runs_by_month;
    }

    public void setRuns_by_month(int[] runs_by_month) {
        this.runs_by_month = runs_by_month;
    }

    public int[] getTime_run_by_month() {
        return time_run_by_month;
    }

    public void setTime_run_by_month(int[] time_run_by_month) {
        this.time_run_by_month = time_run_by_month;
    }

    public float[] getDistance_by_month() {
        return distance_by_month;
    }

    public void setDistance_by_month(float[] distance_by_month) {
        this.distance_by_month = distance_by_month;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float[] getCalories_by_month() {
        return calories_by_month;
    }

    public void setCalories_by_month(float[] calories_by_month) {
        this.calories_by_month = calories_by_month;
    }
}
