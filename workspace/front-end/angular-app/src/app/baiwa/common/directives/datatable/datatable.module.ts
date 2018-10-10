import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { DatatableDirective } from "./datatable.directive";
import { DatatableSearchComponent } from "./datatable-search.component";
import { DatatableFooterComponent } from "./datatable-footer.component";

@NgModule({
    imports: [CommonModule],
    declarations: [DatatableDirective, DatatableSearchComponent, DatatableFooterComponent],
    exports: [DatatableDirective, DatatableSearchComponent, DatatableFooterComponent]
})
export class DatatableModule { }