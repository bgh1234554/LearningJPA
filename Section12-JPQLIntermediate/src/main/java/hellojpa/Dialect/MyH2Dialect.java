package hellojpa.Dialect;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

//Hibernate 5 버전에서의 등록 방법\
//이후 persistence.xml에 MyH2Dialect 등록
//public class MyH2Dialect extends H2Dialect {
//    public MyH2Dialect(){
//        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));    }
//}