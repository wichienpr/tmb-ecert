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
    StatementYearBreakPipe
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
        DateFromStringPipe,
        StatementYearBreakPipe
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
        DateFromStringPipe,
        StatementYearBreakPipe
    ]
})
export class PipesModule { }