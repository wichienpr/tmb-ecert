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
    DatetimeFromStringPipe
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
        DatetimeFromStringPipe
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
        DatetimeFromStringPipe
    ]
})
export class PipesModule { }