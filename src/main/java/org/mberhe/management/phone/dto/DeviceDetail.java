package org.mberhe.management.phone.dto;

public record DeviceDetail(
  String brand,
  String model,
  String assignedId,
  PhoneAvailability phoneAvailability,
  String bookedBy,
  String technology,
  String twoGBands,
  String threeGBands,
  String fourGBands
) {
  public static DeviceDetail from(DeviceDetail deviceDetail, Boolean isAvailable, String bookedBy) {
    return new DeviceDetail(
      deviceDetail.brand(),
      deviceDetail.model(),
      deviceDetail.assignedId(),
      isAvailable ? PhoneAvailability.YES : PhoneAvailability.NO,
      isAvailable ? null : bookedBy,
      deviceDetail.technology(),
      deviceDetail.twoGBands(),
      deviceDetail.threeGBands(),
      deviceDetail.fourGBands()
    );
  }
}

