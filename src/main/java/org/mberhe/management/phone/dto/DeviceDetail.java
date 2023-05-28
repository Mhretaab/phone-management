package org.mberhe.management.phone.dto;

public record DeviceDetail(
  String brand,
  String model,
  String assignedId,
  AvailabilityStatus availabilityStatus,
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
      isAvailable ? AvailabilityStatus.YES : AvailabilityStatus.NO,
      isAvailable ? null : bookedBy,
      deviceDetail.technology(),
      deviceDetail.twoGBands(),
      deviceDetail.threeGBands(),
      deviceDetail.fourGBands()
    );
  }
}

