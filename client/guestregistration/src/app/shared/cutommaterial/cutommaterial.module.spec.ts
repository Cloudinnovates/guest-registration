import { CutommaterialModule } from './cutommaterial.module';

describe('CutommaterialModule', () => {
  let cutommaterialModule: CutommaterialModule;

  beforeEach(() => {
    cutommaterialModule = new CutommaterialModule();
  });

  it('should create an instance', () => {
    expect(cutommaterialModule).toBeTruthy();
  });
});
