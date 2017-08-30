package at.jku.sea.cloud.implementation.sql;

public class SQLTest {
  public static void main(final String[] args) {
    final SQLDataStorage sql = new SQLDataStorage();
    sql.truncateAll();
  }
}
