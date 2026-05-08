package org.example.course;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    private CourseService courseService;

    private static Course course(Long id, String name, int credits, boolean active) {
        return new Course(id, name, credits, active);
    }

    @BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepository);
    }

    @Test
    void findActiveCoursesReturnsOnlyActiveCoursesInRepositoryOrder() {
        Course inactive = course(1L, "Archived Course", 2, false);
        Course firstActive = course(2L, "Algorithms", 6, true);
        Course secondActive = course(3L, "Databases", 5, true);
        when(courseRepository.findAll()).thenReturn(List.of(inactive, firstActive, secondActive));

        List<Course> result = courseService.findActiveCourses();

        assertEquals(List.of(firstActive, secondActive), result);
        verify(courseRepository).findAll();
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findActiveCoursesReturnsEmptyListWhenNoCoursesAreActive() {
        when(courseRepository.findAll()).thenReturn(List.of(
                course(1L, "Archived Course", 2, false),
                course(2L, "Inactive Course", 4, false)
        ));

        List<Course> result = courseService.findActiveCourses();

        assertEquals(List.of(), result);
        verify(courseRepository).findAll();
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findByIdReturnsCourseWhenRepositoryFindsIt() {
        Course expected = course(10L, "Software Testing", 6, true);
        when(courseRepository.findById(10L)).thenReturn(Optional.of(expected));

        Course result = courseService.findById(10L);

        assertSame(expected, result);
        verify(courseRepository).findById(10L);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void findByIdThrowsWhenCourseDoesNotExist() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> courseService.findById(99L));

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(99L);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void createCourseSavesNewActiveCourse() {
        Course savedCourse = course(20L, "Distributed Systems", 8, true);
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        Course result = courseService.createCourse("Distributed Systems", 8);

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).save(courseCaptor.capture());
        Course courseToSave = courseCaptor.getValue();

        assertSame(savedCourse, result);
        assertAll(
                () -> assertNull(courseToSave.getId()),
                () -> assertEquals("Distributed Systems", courseToSave.getName()),
                () -> assertEquals(8, courseToSave.getCredits()),
                () -> assertTrue(courseToSave.isActive())
        );
        verifyNoMoreInteractions(courseRepository);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void createCourseRejectsMissingName(String name) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.createCourse(name, 3)
        );

        assertEquals("Course name is required", exception.getMessage());
        verify(courseRepository, never()).save(any());
        verifyNoMoreInteractions(courseRepository);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    void createCourseRejectsNonPositiveCredits(int credits) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.createCourse("Networks", credits)
        );

        assertEquals("Credits must be positive", exception.getMessage());
        verify(courseRepository, never()).save(any());
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void deleteCourseDeletesExistingCourseByResolvedId() {
        when(courseRepository.findById(30L)).thenReturn(Optional.of(course(30L, "Compilers", 6, true)));

        courseService.deleteCourse(30L);

        verify(courseRepository).findById(30L);
        verify(courseRepository).deleteById(30L);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void deleteCourseDoesNotDeleteWhenCourseDoesNotExist() {
        when(courseRepository.findById(404L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> courseService.deleteCourse(404L));

        assertEquals("Course not found", exception.getMessage());
        verify(courseRepository).findById(404L);
        verify(courseRepository, never()).deleteById(any());
        verifyNoMoreInteractions(courseRepository);
    }
}
