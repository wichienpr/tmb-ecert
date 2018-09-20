import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { RouterModule, Routes } from "@angular/router";

// Components
import { HomeComponent } from "projects/pages/home/home.component";
import { MonitoringPerformaComponent } from "projects/performa/monitoring-performa/monitoring-performa.component";
import { ECertificateDayComponent } from "projects/report/e-certificate-day/e-certificate-day.component";
import { ECertificateMonthlyComponent } from "projects/report/e-certificate-monthly/e-certificate-monthly.component";
import { ClientSignedComponent } from "projects/new-Request/client-signed/client-signed.component";
import { DescriptionComponent } from "projects/performa/description/description.component";
import { OutputVATComponent } from "projects/report/output-vat/output-vat.component";
import { CauseMonthlyComponent } from "projects/report/e-certificate-monthly/cause-monthly/cause-monthly.component";
import { LayoutComponent } from "projects/layout/layout.component";
import { ParameterConfigurationComponent } from "projects/setup/parameter-configuration/parameter-configuration.component";
import { RoleManagementComponent } from "projects/setup/role-management/role-management.component";
import { AuditLogComponent } from "projects/audit-Log/audit-log/audit-log.component";
import { MonitoringComponent } from "projects/batch-Monitoring/monitoring/monitoring.component";
import { AddRoleComponent } from "projects/setup/role-management/add-role/add-role.component";
import { EmailConfigurationComponent } from "projects/setup/email-configuration/email-configuration.component";
import { EditRoleComponent } from "projects/setup/role-management/edit-role/edit-role.component";
import { EditEmailComponent } from "projects/setup/email-configuration/edit-email/edit-email.component";

// Custom Conponent Modules
import { ModalModule } from "../components";

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
            { path: "cause-monthly", component: CauseMonthlyComponent },
            { path: "parameter-Configuration", component: ParameterConfigurationComponent },
            { path: "role-Management", component: RoleManagementComponent },
            { path: "auditLog", component: AuditLogComponent },
            { path: "monitoring", component: MonitoringComponent },
            { path: "add-Role", component: AddRoleComponent },
            { path: "email-configuration", component: EmailConfigurationComponent },
            { path: "edit-role", component: EditRoleComponent },
            { path: "edit-email", component: EditEmailComponent },
        ]
    },
];
@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CommonModule,
        ModalModule,
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
        CauseMonthlyComponent,
        ParameterConfigurationComponent,
        RoleManagementComponent,
        AuditLogComponent,
        MonitoringComponent,
        AddRoleComponent,
        EmailConfigurationComponent,
        EditRoleComponent,
        EditEmailComponent
    ],
    exports: [RouterModule]
})
export class LayoutRoutingModule { }
