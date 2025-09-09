package controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import model.Member;


/**
 * Manages interactions using the member class.
 */
public class MemberManager {
  private final Map<String, Member> membersById = new HashMap<>();
  private final Map<String, Member> membersByEmail = new HashMap<>();
  private final Map<String, Member> membersByPhone = new HashMap<>();
  private final Random rnd = new Random();

  // Default constructor.
  public MemberManager() {}


  /**
   * Copy constructor
   *
   * <p>shallow clone of managers that creates new Member instances
   * using Member(Member) so that the new MemberManager does not reference the
   * exact same Member objects as the source. Owned items are intentionally not
   * deep-copied to avoid recursion.
   *
   * @param other --> instance to copy.
   */
  public MemberManager(MemberManager other) {
    for (Member m : other.listMembers()) {
      Member copy = new Member(m);
      membersById.put(copy.getMemberId(), copy);
      membersByEmail.put(copy.getEmail(), copy);
      membersByPhone.put(copy.getPhone(), copy);
    }
  }

  /**
   * Help create a new member, adding them into the programme.
   *
   * @param name        --> of the user.
   * @param email       --> of the user.
   * @param phone       --> of the user.
   * @param currentDay  --> The current day, store creation date of the acount.
   * @return            --> the new member.
   */
  public Member createMember(String name, String email, String phone, int currentDay) {
    if (membersByEmail.containsKey(email)) {
      throw new IllegalArgumentException("Email already exists");
    }

    if (membersByPhone.containsKey(phone)) {
      throw new IllegalArgumentException("Phone already exists");
    }

    String id;
    do {
      id = generateId();
    } while (membersById.containsKey(id));
    Member m = new Member(id, name, email, phone, currentDay);
    membersById.put(id, m);
    membersByEmail.put(email, m);
    membersByPhone.put(phone, m);
    return m;
  }

  /**
   * Randomly generated id for users.
   *
   * @return    --> The random id.
   */
  public String generateId() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder(6);
    for (int i = 0; i < 6; i++) {
      sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
  }

  /**
   * Remove a member from the "database".
   *
   * @param memberId    --> Of the member to remove.
   */
  public void deleteMember(String memberId) {
    Member m = membersById.remove(memberId);
    if (m == null) {
      throw new IllegalArgumentException("Member not found");
    } else if (!m.getOwnedItems().isEmpty()) {
      throw new IllegalStateException("Menber owns items, remove or transfer before deleation.");
    } else {
      membersById.remove(memberId);
      membersByEmail.remove(m.getEmail());
      membersByPhone.remove(m.getPhone());
    }
  }

  public Member getMemberById(String id) {
    return membersById.get(id);
  }

  public Member getMemberByEmail(String email) {
    return membersByEmail.get(email);
  }

  public Member getMemberByPhone(String phone) {
    return membersByPhone.get(phone);
  }

  public Collection<Member> listMembers() {
    return Collections.unmodifiableCollection(membersById.values());
  }

  /**
   * Change info of a pre-existing member.
   *
   * @param memberId    --> The id of the users whos info needs changing.
   * @param newName     --> The new name of the user.
   * @param newEmail    --> New email.
   * @param newPhone    --> New phone number.
   */
  public void updateMemberInfo(String memberId, String newName, String newEmail, String newPhone) {
    Member m = membersById.get(memberId);
    if (m == null) {
      throw new IllegalArgumentException("Member not found");
    }

    if (!m.getEmail().equals(newEmail) && membersByEmail.containsKey(newEmail)) {
      throw new IllegalArgumentException("Email already exists");
    } else if (!m.getPhone().equals(newPhone) && membersByPhone.containsKey(newPhone)) {
      throw new IllegalArgumentException("Phone already exists");
    }

    membersByEmail.remove(m.getEmail());
    membersByPhone.remove(m.getPhone());
    m.setName(newName);
    m.setEmail(newEmail);
    m.setPhone(newPhone);
    membersByEmail.put(newEmail, m);
    membersByPhone.put(newPhone, m);
  }
}
