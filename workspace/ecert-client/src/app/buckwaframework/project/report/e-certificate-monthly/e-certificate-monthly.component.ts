import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-e-certificate-monthly',
  templateUrl: './e-certificate-monthly.component.html',
  styleUrls: ['./e-certificate-monthly.component.css']
})
export class ECertificateMonthlyComponent implements OnInit {
  showData: boolean = false; 
  constructor() { }

  ngOnInit() {
  }

  
  searchData(): void {
    this.showData = true;
  }
  
  clearData(): void {
    this.showData = false;
  }

}
