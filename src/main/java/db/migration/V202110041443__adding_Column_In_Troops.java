package db.migration;

import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;

public class V202110041443__adding_Column_In_Troops extends BaseMigration{

  @Override
  public void migrate(DSLContext dslContext) {
    dslContext.alterTable("troop").addColumn("in_army", SQLDataType.BOOLEAN).execute();
  }
}
