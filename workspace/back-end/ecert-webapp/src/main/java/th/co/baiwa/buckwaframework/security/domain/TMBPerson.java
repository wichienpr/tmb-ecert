package th.co.baiwa.buckwaframework.security.domain;

import java.io.Serializable;
import java.util.List;

public class TMBPerson implements Serializable {
    private static final long serialVersionUID = 1L;
    private String useranme;
    private String password;
    private String tmbcn;
    private String userid;
    private String name;
    private String group;
	private String branchCode;
    private List<String> memberOfs;//memberO

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getMemberOfs() {
        return memberOfs;
    }

    public void setMemberOfs(List<String> memberOfs) {
        this.memberOfs = memberOfs;
    }

    public String getTmbcn() {
        return tmbcn;
    }

    public void setTmbcn(String tmbcn) {
        this.tmbcn = tmbcn;
    }

    public String getUseranme() {
        return useranme;
    }

    public void setUseranme(String useranme) {
        this.useranme = useranme;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public String toString() {
		return "TMBPerson [useranme=" + useranme + ", password=" + password + ", tmbcn=" + tmbcn + ", userid=" + userid
				+ ", name=" + name + ", group=" + group + ", memberOfs=" + memberOfs + "]";
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
    
    
}
