package controller.dto;

import model.Member;

/**
 * Immutable snapshot (DTO) of a member for read-only exposure.
 * Contains only primative fields to avoid leaking internal mutable state.
 */
public class MemberDto {
  private final String memberId;
  private final String name;
  private final String email;
  private final String phone;
  private final int credits;
  private final int creationDay;

  /**
   * Functionally a copy constructor.
   *
   * @param m   --> member to copy fields from.
   */
  public MemberDto(Member m) {
    this.memberId = m.getMemberId();
    this.name = m.getName();
    this.email = m.getEmail();
    this.phone = m.getPhone();
    this.credits = m.getCredits();
    this.creationDay = m.getCreationDate();
  }

  public String getMemberId() {
    return memberId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public int getCredits() {
    return credits;
  }

  public int getCreationDate() {
    return creationDay;
  }
}
