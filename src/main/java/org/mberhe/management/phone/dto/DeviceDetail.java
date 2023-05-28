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
}

