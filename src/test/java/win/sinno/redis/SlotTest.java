package win.sinno.redis;

import org.junit.Test;

/**
 * win.sinno.redis.SlotTest
 *
 * @author chenlizhong@qipeng.com
 * @date 2018/4/18
 */
public class SlotTest {

  @Test
  public void printSlot() {

    StringBuilder sb = new StringBuilder();
    int i = 5;
    for (; i <= 5461; i++) {
      sb.append(i);
      sb.append(" ");
    }
    System.out.println(sb.toString());

    sb.setLength(0);
    for (; i <= 10922; i++) {
      sb.append(i);
      sb.append(" ");
    }
    System.out.println(sb.toString());

    sb.setLength(0);
    for (; i <= 16383; i++) {
      sb.append(i);
      sb.append(" ");
    }
    System.out.println(sb.toString());

  }
}
