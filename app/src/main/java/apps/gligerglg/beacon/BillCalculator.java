package apps.gligerglg.beacon;

/**
 * Created by Gayan Lakshitha on 3/21/2018.
 */

public class BillCalculator {

    //Fixed Charges
    private static final double FIXED_DOMESTIC_0TO30  = 30.0;
    private static final double FIXED_DOMESTIC_31TO60  = 60.0;
    private static final double FIXED_DOMESTIC_61TO90  = 90.0;
    private static final double FIXED_DOMESTIC_91TO120  = 480.0;
    private static final double FIXED_DOMESTIC_121TO180  = 480.0;
    private static final double FIXED_DOMESTIC_OVER180  = 540.0;
    private static final double FIXED_RELIGIOUS_0TO30  = 30.0;
    private static final double FIXED_RELIGIOUS_31TO90  = 60.0;
    private static final double FIXED_RELIGIOUS_91TO120  = 180.0;
    private static final double FIXED_RELIGIOUS_121TO180  = 180.0;
    private static final double FIXED_RELIGIOUS_OVER180  = 240.0;
    private static final double FIXED_GENERAL  = 240.0;
    private static final double FIXED_HOTEL  = 600.0;
    private static final double FIXED_INDUSTRIAL  = 600.0;

    //Unit Charges
    private static final double UNIT_DOMESTIC_0TO30  = 2.50;
    private static final double UNIT_DOMESTIC_31TO60  = 4.85;
    private static final double UNIT_DOMESTIC_0TO60  = 7.85;
    private static final double UNIT_DOMESTIC_61TO90  = 10.0;
    private static final double UNIT_DOMESTIC_91TO120  = 27.75;
    private static final double UNIT_DOMESTIC_121TO180  = 32.0;
    private static final double UNIT_DOMESTIC_OVER180  = 45.0;
    private static final double UNIT_RELIGIOUS_0TO30  = 1.90;
    private static final double UNIT_RELIGIOUS_31TO90  = 2.80;
    private static final double UNIT_RELIGIOUS_91TO120  = 6.75;
    private static final double UNIT_RELIGIOUS_121TO180  = 7.50;
    private static final double UNIT_RELIGIOUS_OVER180  = 9.40;
    private static final double UNIT_GENERAL_1  = 18.30;
    private static final double UNIT_GENERAL_2  = 22.85;
    private static final double UNIT_HOTEL  = 21.50;
    private static final double UNIT_INDUSTRIAL_1  = 10.80;
    private static final double UNIT_INDUSTRIAL_2  = 12.20;

    public static double calcDomesticBill(int units, int days){
        if(units>(2*days))
            return calcDomesticHigherMethod(units,days);
        else
            return calcDomesticLowerMethod(units,days);
    }

    private static double calcDomesticHigherMethod(int units, int days){
        double total = 0;

        //Block1 Calculation
        total += (2*days) * UNIT_DOMESTIC_0TO60;
        units -= (2*days);

        //Block2 Calculation
        if(units<days){
            total += units * UNIT_DOMESTIC_61TO90;
            total += FIXED_DOMESTIC_61TO90;
            return total;
        }else
        {
            total += days * UNIT_DOMESTIC_61TO90;
            units -= days;
        }

        //Block3 Calculation
        if(units<days){
            total += units * UNIT_DOMESTIC_91TO120;
            total += FIXED_DOMESTIC_91TO120;
            return total;
        }
        else {
            total += days * UNIT_DOMESTIC_91TO120;
            units -= days;
        }

        //Block4 Calculation
        if(units<(2*days)){
            total += units * UNIT_DOMESTIC_121TO180;
            total += FIXED_DOMESTIC_121TO180;
            return total;
        }
        else {
            total += (2*days) * UNIT_DOMESTIC_121TO180;
            units -= (2*days);
        }

        //Block5 Calculation
        total += units * UNIT_DOMESTIC_OVER180;
        total += FIXED_DOMESTIC_OVER180;
        return total;
    }

    private static double calcDomesticLowerMethod(int units, int days){
        double total = 0;

        if(units>days){
            total += days * UNIT_DOMESTIC_0TO30;
            units -= days;
            total += units * UNIT_DOMESTIC_31TO60;
            total += FIXED_DOMESTIC_31TO60;
        }
        else{
           total += units * UNIT_DOMESTIC_0TO30;
           total += FIXED_DOMESTIC_0TO30;
        }

        return total;
    }

    public static double calcReligiousBill(int units, int days){
        double total = 0;

        //Block1 Calculation
        if(units<days){
            total += units * UNIT_RELIGIOUS_0TO30;
            total += FIXED_RELIGIOUS_0TO30;
            return total;
        }
        else {
            total += days * UNIT_RELIGIOUS_0TO30;
            units -= days;
        }

        //Block2 Calculation
        if(units<(2*days)){
            total += units * UNIT_RELIGIOUS_31TO90;
            total += FIXED_RELIGIOUS_31TO90;
            return total;
        }
        else {
            total += (2*days) * UNIT_RELIGIOUS_31TO90;
            units -= (2*days);
        }

        //Block3 Calculation
        if(units<days){
            total += units * UNIT_RELIGIOUS_91TO120;
            total += FIXED_RELIGIOUS_91TO120;
            return total;
        }
        else {
            total += days * UNIT_RELIGIOUS_91TO120;
            units -= days;
        }

        //Block4 Calculation
        if(units<(2*days)){
            total += units * UNIT_RELIGIOUS_121TO180;
            total += FIXED_RELIGIOUS_121TO180;
            return total;
        }
        else {
            total += (2*days) * UNIT_RELIGIOUS_121TO180;
            units -= (2*days);
        }

        //Block5 Calculation
        total += units * UNIT_RELIGIOUS_OVER180;
        total += FIXED_RELIGIOUS_OVER180;
        return total;
    }

    public static double calcGeneralBill(int units, int days){
        double total = 0;

        if(units<=(days*10))
            total += units * UNIT_GENERAL_1;
        else
            total += units * UNIT_GENERAL_2;

        total += FIXED_GENERAL;
        return total;
    }

    public static double calcHotelBill(int units){
        double total = 0;
        total += units * UNIT_HOTEL;
        total += FIXED_HOTEL;
        return total;
    }

    public static double calcIndustrialBill(int units, int days){
        double total = 0;

        if(units<=(days*10))
            total += units * UNIT_INDUSTRIAL_1;
        else
            total += units * UNIT_INDUSTRIAL_2;

        total += FIXED_INDUSTRIAL;
        return total;
    }
}
