import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "../../project/pages/home/home.component";
import { MonitoringPerformaComponent } from "../../project/performa/monitoring-performa/monitoring-performa.component";
import { ECertificateDayComponent } from "../../project/report/e-certificate-day/e-certificate-day.component";
import { ECertificateMonthlyComponent } from "../../project/report/e-certificate-monthly/e-certificate-monthly.component";
import { ClientSignedComponent } from "../../project/new-Request/client-signed/client-signed.component";
import { DescriptionComponent } from "../../project/performa/description/description.component";
import { OutputVATComponent } from "../../project/report/output-vat/output-vat.component";
import { CauseMonthlyComponent } from "../../project/report/e-certificate-monthly/cause-monthly/cause-monthly.component";
import { LayoutComponent } from "../../project/layout/layout.component";
import { ComponentsModule } from "../../common/components/components.module";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
const routes: Routes = [
    {
        path: "",
        component: LayoutComponent,
        children: [
            { path: "home", component: HomeComponent },
            { path: "performa", component: MonitoringPerformaComponent },
            { path: "e-certificate-day", component: ECertificateDayComponent },
            { path: "e-certificate-monthly", component: ECertificateMonthlyComponent },
            { path: "client-signed", component: ClientSignedComponent },
            { path: "description", component: DescriptionComponent },
            { path: "output-VAT", component: OutputVATComponent },
            { path: "cause-monthly", component: CauseMonthlyComponent }
        ]
    },
];
@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CommonModule,
        ComponentsModule,
        FormsModule
    ],
    declarations: [
        LayoutComponent,
        HomeComponent,
        MonitoringPerformaComponent,
        ECertificateDayComponent,
        ECertificateMonthlyComponent,
        ClientSignedComponent,
        DescriptionComponent,
        OutputVATComponent,
        CauseMonthlyComponent
    ],
    exports: [RouterModule]
})
export class LayoutRoutingModule { }
