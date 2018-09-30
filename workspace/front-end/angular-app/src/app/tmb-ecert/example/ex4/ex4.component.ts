import { Component, OnInit } from '@angular/core';
import { Ex4Service } from './ex4.service';

@Component({
  selector: 'app-ex4',
  templateUrl: './ex4.component.html',
  styleUrls: ['./ex4.component.css']
})
export class Ex4Component implements OnInit {

  constructor(private ex4Service: Ex4Service) {
    ex4Service.findByCode("50001");
  }

  ngOnInit() {

  }

}
