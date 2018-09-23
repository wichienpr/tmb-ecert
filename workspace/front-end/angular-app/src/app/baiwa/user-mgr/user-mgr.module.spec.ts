import { UserMgrModule } from './user-mgr.module';

describe('UserMgrModule', () => {
  let userMgrModule: UserMgrModule;

  beforeEach(() => {
    userMgrModule = new UserMgrModule();
  });

  it('should create an instance', () => {
    expect(userMgrModule).toBeTruthy();
  });
});
