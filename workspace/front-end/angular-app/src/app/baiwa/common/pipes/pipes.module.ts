import { NgModule } from "@angular/core";
import {
    AccountNumberPipe,
    DateStringPipe,
    DecimalFormatPipe,
    EmptyStringPipe,
    ExponentialStrengthPipe,
    CommaStringBrPipe,
    ThaiMoneyString,
    DatetimeStringPipe,
    TmbReqNumberPipe,
    DatetimeFromStringPipe,
    DateFromStringPipe,
} from "pipes/";

@NgModule({
    imports: [],
    declarations: [
        AccountNumberPipe,
        DateStringPipe,
        DecimalFormatPipe,
        EmptyStringPipe,
        ExponentialStrengthPipe,
        CommaStringBrPipe,
        ThaiMoneyString,
        DatetimeStringPipe,
        TmbReqNumberPipe,
        DatetimeFromStringPipe,
        DateFromStringPipe
    ],
    exports: [
        AccountNumberPipe,
        DateStringPipe,
        DecimalFormatPipe,
        EmptyStringPipe,
        ExponentialStrengthPipe,
        CommaStringBrPipe,
        ThaiMoneyString,
        DatetimeStringPipe,
        TmbReqNumberPipe,
        DatetimeFromStringPipe,
        DateFromStringPipe
    ]
})
export class PipesModule { }