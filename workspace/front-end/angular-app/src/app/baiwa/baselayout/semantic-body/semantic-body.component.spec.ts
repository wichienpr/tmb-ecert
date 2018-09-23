import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SemanticBodyComponent } from './semantic-body.component';

describe('SemanticBodyComponent', () => {
  let component: SemanticBodyComponent;
  let fixture: ComponentFixture<SemanticBodyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SemanticBodyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SemanticBodyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
