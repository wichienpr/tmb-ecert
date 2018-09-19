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
import { ParameterConfigurationComponent } from "../../project/setup/parameter-configuration/parameter-configuration.component";
import { RoleManagementComponent } from "../../project/setup/role-management/role-management.component";
import { AuditLogComponent } from "../../project/audit-Log/audit-log/audit-log.component";
import { MonitoringComponent } from "../../project/batch-Monitoring/monitoring/monitoring.component";
import { AddRoleComponent } from "../../project/setup/role-management/add-role/add-role.component";
import { EmailConfigurationComponent } from "../../project/setup/email-configuration/email-configuration.component";
import { EditRoleComponent } from "../../project/setup/role-management/edit-role/edit-role.component";
import { EditEmailComponent } from "../../project/setup/email-configuration/edit-email/edit-email.component";


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
            { path: "email-configuration", component:  EmailConfigurationComponent } ,
            { path: "edit-role", component:  EditRoleComponent } ,
            { path: "edit-email", component:  EditEmailComponent } ,
          
            
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
