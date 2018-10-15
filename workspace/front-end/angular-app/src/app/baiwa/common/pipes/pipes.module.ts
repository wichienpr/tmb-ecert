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
    TmbReqNumberPipe
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
        TmbReqNumberPipe
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
        TmbReqNumberPipe
    ]
})
export class PipesModule { }