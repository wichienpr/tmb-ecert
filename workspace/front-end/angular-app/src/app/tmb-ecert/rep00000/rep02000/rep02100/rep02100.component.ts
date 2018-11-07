import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Rep02100Service } from 'app/tmb-ecert/rep00000/rep02000/rep02100/rep02100.service';
import { FormGroup } from '@angular/forms';
import { AjaxService } from "services/";
import { Rep02100 } from './rep02100.model';

@Component({
  selector: 'app-rep02100',
  templateUrl: './rep02100.component.html',
  styleUrls: ['./rep02100.component.css'],
  providers: [Rep02100Service]
})
export class Rep02100Component implements OnInit {

  showData: boolean = false;
  form: FormGroup;
  dataT: Rep02100[] = [];
  loading: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Rep02100Service,
    private ajax: AjaxService
  ) {

    this.service.getForm().subscribe(form => {
      this.form = form
    });

  }

  ngOnInit() {
    let custsegmentCode = this.route.snapshot.queryParams["custsegmentCode"];
    this.form.controls[`custsegmentCode`].setValue(custsegmentCode);
    let dForm = this.route.snapshot.queryParams["dateForm"];
    let dTo = this.route.snapshot.queryParams["dateTo"];
    if (dForm != "" || dTo != "") {
      this.form.controls[`dateForm`].setValue(dForm);
      this.form.controls[`dateTo`].setValue(dTo);
      this.searchData();
    }
    this.getData();
  }

  ngAfterViewInit() { }

  searchData(): void {
    this.showData = true;
  }

  clearData(): void {
    this.showData = false;
  }

  getData() {
    this.loading = true;
    this.dataT = [];
    const URL = "/api/rep/rep02100/list";
    this.ajax.post(URL, {
      custsegmentCode: this.form.controls.custsegmentCode.value,
      dateForm: this.form.controls.dateForm.value,
      dateTo: this.form.controls.dateTo.value
    }, res => {
      const data = res.json() as Rep02100[];
      setTimeout(() => {
        this.loading = false;
      }, 200);
      data.forEach(element => {
        this.dataT.push(element);
      });
    });
  }

  clickBack() {
    this.router.navigate(['/rep/rep02000'], {
      queryParams: {
        dateForm: this.form.controls.dateForm.value,
        dateTo: this.form.controls.dateTo.value
      }
    });
  }

}
