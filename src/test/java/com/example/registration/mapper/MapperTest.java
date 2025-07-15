package com.example.registration.mapper;

import com.example.registration.dto.PhoneRequestDTO;
import com.example.registration.dto.UserRequestDTO;
import com.example.registration.model.Phone;
import com.example.registration.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MapperTest {

    private UserMapper userMapper;
    private PhoneMapper phoneMapper;

    @BeforeEach
    void setup() {
        userMapper = Mappers.getMapper(UserMapper.class);
        phoneMapper = Mappers.getMapper(PhoneMapper.class);
    }

    @Test
    void testUserRequestDTOToUser() {
        // Create a UserRequestDTO with the new field names
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .name("Juan Rodriguez")
                .email("juan@rodriguez.org")
                .password("hunter2")
                .build();

        // Map the DTO to a User
        User user = userMapper.userRequestDTOToUser(userRequestDTO);

        // Verify that the User has the correct field values
        assertNotNull(user);
        assertEquals("Juan Rodriguez", user.getFullName());
        assertEquals("juan@rodriguez.org", user.getUserEmail());
        assertEquals("hunter2", user.getUserPassword());

        System.out.println("[DEBUG_LOG] User mapping test passed");
    }

    @Test
    void testPhoneRequestDTOToPhone() {
        // Create a PhoneRequestDTO with the new field names
        PhoneRequestDTO phoneRequestDTO = PhoneRequestDTO.builder()
                .number("1234567")
                .citycode("1")
                .contrycode("57")
                .build();

        // Map the DTO to a Phone
        Phone phone = phoneMapper.phoneRequestDTOToPhone(phoneRequestDTO);

        // Verify that the Phone has the correct field values
        assertNotNull(phone);
        assertEquals("1234567", phone.getPhoneNumber());
        assertEquals("1", phone.getCityCode());
        assertEquals("57", phone.getCountryCode());

        System.out.println("[DEBUG_LOG] Phone mapping test passed");
    }

    @Test
    void testPhoneRequestDTOsToPhones() {
        // Create a list of PhoneRequestDTOs with the new field names
        List<PhoneRequestDTO> phoneRequestDTOs = new ArrayList<>();
        phoneRequestDTOs.add(PhoneRequestDTO.builder()
                .number("1234567")
                .citycode("1")
                .contrycode("57")
                .build());
        phoneRequestDTOs.add(PhoneRequestDTO.builder()
                .number("7654321")
                .citycode("2")
                .contrycode("58")
                .build());

        // Map the DTOs to Phones
        List<Phone> phones = phoneMapper.phoneRequestDTOsToPhones(phoneRequestDTOs);

        // Verify that the Phones have the correct field values
        assertNotNull(phones);
        assertEquals(2, phones.size());
        assertEquals("1234567", phones.get(0).getPhoneNumber());
        assertEquals("1", phones.get(0).getCityCode());
        assertEquals("57", phones.get(0).getCountryCode());
        assertEquals("7654321", phones.get(1).getPhoneNumber());
        assertEquals("2", phones.get(1).getCityCode());
        assertEquals("58", phones.get(1).getCountryCode());

        System.out.println("[DEBUG_LOG] Phone list mapping test passed");
    }
}
