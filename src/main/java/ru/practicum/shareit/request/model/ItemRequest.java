package ru.practicum.shareit.request.model;

import lombok.Data;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private Date created;
}
