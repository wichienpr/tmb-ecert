import { BaselayoutModule } from './baselayout.module';

describe('BaselayoutModule', () => {
  let baselayoutModule: BaselayoutModule;

  beforeEach(() => {
    baselayoutModule = new BaselayoutModule();
  });

  it('should create an instance', () => {
    expect(baselayoutModule).toBeTruthy();
  });
});
